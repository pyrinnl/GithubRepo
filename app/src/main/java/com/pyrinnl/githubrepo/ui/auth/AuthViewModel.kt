package com.pyrinnl.githubrepo.ui.auth

import androidx.annotation.StringRes
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyrinnl.githubrepo.R
import com.pyrinnl.githubrepo.model.AppRepository
import com.pyrinnl.githubrepo.model.EmptyFieldException
import com.pyrinnl.githubrepo.model.InvalidCredentialsException
import com.pyrinnl.githubrepo.model.InvalidInputException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    val token: MutableLiveData<String> = MutableLiveData("")

    private val _state: MutableLiveData<State> = MutableLiveData(State.Idle)
    val state: LiveData<State> = _state

    private val _actions: Channel<Action> = Channel(Channel.BUFFERED)
    val actions: Flow<Action> = _actions.receiveAsFlow()


    fun onSignButtonPressed() {

        viewModelScope.launch {
            try {
                _state.value = State.Loading
                appRepository.signIn(token.value ?: "")
                _actions.send(Action.RouteToMain)
            } catch (e: EmptyFieldException) {
                _state.value = State.InvalidInput(R.string.field_is_empty)
            } catch (e: InvalidInputException) {
                _state.value = State.InvalidInput(R.string.invalid_input)
            } catch (e: InvalidCredentialsException) {
                _state.value = State.InvalidInput(R.string.invalid_token)
            }
        }
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        data class InvalidInput(@StringRes val reason: Int) : State
    }

    sealed interface Action {
        data class ShowError(@StringRes val message: Int) : Action
        object RouteToMain : Action
    }
}