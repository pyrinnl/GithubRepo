package com.pyrinnl.githubrepo.ui.detail

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyrinnl.githubrepo.R
import com.pyrinnl.githubrepo.model.*
import com.pyrinnl.githubrepo.model.entities.Readme
import com.pyrinnl.githubrepo.model.entities.RepoDetails
import com.pyrinnl.githubrepo.utills.toMarkdown
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RepositoryInfoViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = _state

    private var repositoryDetails: RepoDetails? = null


    init {
        initRepoDetails()
    }

    fun onRetryRepoDetailsPressed(repoName: String) {
        viewModelScope.safeGetRepoDetails { appRepository.getRepository(repoName) }
    }

    fun onRetryReadmeButtonPressed(repoName: String) {
        repositoryDetails?.safeGetReadme {
            appRepository.getRepositoryReadme(repoName)
        }
    }

    fun logout() {
        appRepository.logout()
    }

    private fun CoroutineScope.safeGetRepoDetails(block: suspend CoroutineScope.() -> RepoDetails) {
        this.launch {
            val errorTexts: ErrorTexts
            try {
                _state.value = State.Loading
                val repoDetails = block.invoke(this)
                _state.value = State.Loaded(repoDetails, ReadmeState.Loading)
            } catch (e: ConnectionException) {
                errorTexts = processConnectionException()
                _state.value = State.Error(errorTexts)
            } catch (e: Exception) {
                errorTexts = processInternalException()
                _state.value = State.Error(errorTexts)
            }
        }
    }

    private fun RepoDetails.safeGetReadme(
        block: suspend CoroutineScope.() -> Readme
    ) {
        val repoDetails = this
        viewModelScope.launch {
            val errorTexts: ErrorTexts
            try {
                _state.value = State.Loaded(repoDetails, ReadmeState.Loading)
                val markdown = block.invoke(this).content.toMarkdown()
                _state.value = State.Loaded(repoDetails, ReadmeState.Loaded(markdown))
            } catch (e: ConnectionException) {
                errorTexts = processConnectionException()
                _state.value = State.Loaded(repoDetails, ReadmeState.Error(errorTexts))
            } catch (e: EmptyContentException) {
                errorTexts = processEmptyContentException()
                _state.value = State.Loaded(repoDetails, ReadmeState.Empty)
            } catch (e: Exception) {
                errorTexts = processInternalException()
                _state.value = State.Loaded(repoDetails, ReadmeState.Error(errorTexts))
            }
        }

    }

    private fun initRepoDetails() {
        viewModelScope.launch {
            appRepository.listenCurrentRepoName().collect { repoName ->
                viewModelScope.safeGetRepoDetails {
                    appRepository.getRepository(repoName).also { repoDetails ->
                        repositoryDetails = repoDetails
                        repoDetails.safeGetReadme {
                            appRepository.getRepositoryReadme(repoName)
                        }
                    }
                }
            }
        }
    }

    private fun processConnectionException(): ErrorTexts {
        return ErrorTexts(
            title = R.string.connection_error_item_title,
            text = R.string.connection_error_item_desc,
            errorIcon = R.drawable.ic_connection_error
        )
    }

    private fun processEmptyContentException(): ErrorTexts {
        return ErrorTexts(
            title = R.string.something_error_Item_title,
            text = R.string.something_error_item_desc,
            errorIcon = R.drawable.ic_something_error,
        )
    }

    private fun processInternalException(): ErrorTexts {
        return ErrorTexts(
            title = R.string.something_error_Item_title,
            text = R.string.something_error_item_desc,
            errorIcon = R.drawable.ic_something_error,
        )
    }


    sealed interface State {
        object Loading : State
        data class Error(val error: ErrorTexts) : State
        data class Loaded(
            val repoDetails: RepoDetails,
            val readmeState: ReadmeState
        ) : State
    }

    sealed interface ReadmeState {
        object Loading : ReadmeState
        object Empty : ReadmeState
        data class Error(val errorTexts: ErrorTexts) : ReadmeState
        data class Loaded(val markdown: String) : ReadmeState
    }


    class ErrorTexts(
        @StringRes val title: Int,
        @ColorRes val colorTitle: Int = R.color.error,
        @StringRes val text: Int,
        @DrawableRes val errorIcon: Int,
        @StringRes val buttonText: Int = R.string.retry_button
    )

}