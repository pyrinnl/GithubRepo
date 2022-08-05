package com.pyrinnl.githubrepo.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pyrinnl.githubrepo.model.AppRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository
): ViewModel() {

    private val _isSignIn: MutableLiveData<Boolean> = MutableLiveData()
    val isSignedIn: LiveData<Boolean> = _isSignIn

    init {
        _isSignIn.value = appRepository.isSignIn()
    }

}