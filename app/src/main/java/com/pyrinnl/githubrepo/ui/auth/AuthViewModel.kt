package com.pyrinnl.githubrepo.ui.auth

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.Flow

class AuthViewModel : ViewModel() {

    val token: MutableLiveData<String> = MutableLiveData()
    val state: LiveData<State> = TODO()
    val actions: Flow<Action> = TODO()


    fun onSignButtonPressed() {
        // TODO:
    }

    sealed interface State {
        object Idle : State
        object Loading : State
        data class InvalidInput(val reason: String) : State
    }

    sealed interface Action {
        data class ShowError(val message: String): Action
        object RouteToMain: Action
    }

    // TODO:
}