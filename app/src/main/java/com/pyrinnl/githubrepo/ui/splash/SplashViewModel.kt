package com.pyrinnl.githubrepo.ui.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.pyrinnl.githubrepo.model.AppRepository
import com.pyrinnl.githubrepo.model.ConnectionException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val appRepository: AppRepository
) : ViewModel() {

    private val _isSignIn: MutableLiveData<Boolean> = MutableLiveData()
    val isSignedIn: LiveData<Boolean> = _isSignIn

    init {
        _isSignIn.value = appRepository.isSignIn()
    }

    suspend fun getUserInfo() = withContext(Dispatchers.IO) {
        try {
            appRepository.doGetUserInfo()
        } catch (e: ConnectionException) {
            return@withContext
        }
    }
}