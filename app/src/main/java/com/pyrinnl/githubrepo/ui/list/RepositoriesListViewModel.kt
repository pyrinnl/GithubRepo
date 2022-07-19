package com.pyrinnl.githubrepo.ui.list

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.pyrinnl.githubrepo.model.entities.Repo

class RepositoriesListViewModel : ViewModel() {
    val state: LiveData<State> = TODO()

    sealed interface State {
        object Loading : State
        data class Loaded(val repos: List<Repo>) : State
        data class Error(val error: String) : State
        object Empty : State
    }

    // TODO:
}