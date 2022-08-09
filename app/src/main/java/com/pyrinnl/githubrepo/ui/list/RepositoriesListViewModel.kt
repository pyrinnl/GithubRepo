package com.pyrinnl.githubrepo.ui.list

import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyrinnl.githubrepo.R
import com.pyrinnl.githubrepo.model.AppRepository
import com.pyrinnl.githubrepo.model.ConnectionException
import com.pyrinnl.githubrepo.model.EmptyContentException
import com.pyrinnl.githubrepo.model.entities.Repo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RepositoriesListViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _state: MutableLiveData<State> = MutableLiveData()
    val state: LiveData<State> = _state

    init {
        viewModelScope.safeLaunch {
            appRepository.getRepositories()
        }
    }

    fun onRetryButtonPressed() {
        viewModelScope.safeLaunch {
            appRepository.getRepositories()
        }
    }

    fun logout() {
        appRepository.logout()
    }


    private fun CoroutineScope.safeLaunch(block: suspend CoroutineScope.() -> (List<Repo>)) {
        this.launch {
            try {
                _state.value = State.Loading
                val repos = block.invoke(this)
                _state.value = State.Loaded(repos)
            } catch (e: ConnectionException) {
                processConnectionException()
            } catch (e: EmptyContentException) {
                processEmptyContentException()
            } catch (e: Exception) {
                processInternalException()
            }
        }
    }

    private fun processConnectionException() {
        _state.value = State.Error(
            ErrorTexts(
                title = R.string.connection_error_item_title,
                colorTitle = R.color.error,
                text = R.string.connection_error_item_desc,
                errorIcon = R.drawable.ic_connection_error,
                buttonText = R.string.retry_button
            )
        )
    }

    private fun processEmptyContentException() {
        _state.value = State.Error(
            ErrorTexts(
                title = R.string.empty_item_title,
                colorTitle = R.color.blue,
                text = R.string.empty_desc_item,
                errorIcon = R.drawable.ic_empty,
                buttonText = R.string.refresh_button
            )
        )
    }

    private fun processInternalException() {
        _state.value = State.Error(
            ErrorTexts(
                title = R.string.something_error_item_desc,
                colorTitle = R.color.error,
                text = R.string.something_error_item_desc,
                errorIcon = R.drawable.ic_something_error,
                buttonText = R.string.retry_button
            )
        )
    }

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: ErrorTexts) : State
    }

    class ErrorTexts(
        @StringRes val title: Int,
        @ColorRes val colorTitle: Int,
        @StringRes val text: Int,
        @DrawableRes val errorIcon: Int,
        @StringRes val buttonText: Int
    )
}