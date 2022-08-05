package com.pyrinnl.githubrepo.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyrinnl.githubrepo.model.AppRepository
import com.pyrinnl.githubrepo.model.BackendException
import com.pyrinnl.githubrepo.model.entities.RepoDetails
import com.pyrinnl.githubrepo.utills.toMarkdown
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = _state


    fun getRepository(repoName: String) {
        viewModelScope.launch {


            _state.value = State.Loading
            val repoDetails = appRepository.getRepository(repoName)
            _state.value = State.Loaded(repoDetails, ReadmeState.Loading)


        }

        _state.observeForever { state ->
            if (state is State.Loaded) {
                viewModelScope.launch {
                    try {
                        val markdown =
                            appRepository.getRepositoryReadme(repoName).content.toMarkdown()
                        _state.value =
                            State.Loaded(state.repoDetails, ReadmeState.Loaded(markdown))
                    } catch (e: BackendException) {
                        Log.d("TAGTAG", "$e")
                        _state.value = State.Loaded(state.repoDetails, ReadmeState.Empty)
                    }
                }

            }
        }
    }


    sealed interface State {
        object Loading : State
        data class Error(val error: String) : State

        data class Loaded(
            val repoDetails: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val error: String) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }

    override fun onCleared() {
        super.onCleared()
    }
}