package com.pyrinnl.githubrepo.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyrinnl.githubrepo.model.AppRepository
import com.pyrinnl.githubrepo.model.ConnectionException
import com.pyrinnl.githubrepo.model.entities.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = _state


    init {
        viewModelScope.launch {
            try {
                _state.value = State.Loading
                val data = appRepository.getRepositories()
                if(data.isEmpty()) _state.value = State.Empty else _state.value = State.Loaded(data)
            } catch (e: ConnectionException) {
                _state.value = State.Error(e)
            }
        }
    }

    fun logout() {
        appRepository.logout()
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val e: Exception) : State
        object Empty : State
    }
}