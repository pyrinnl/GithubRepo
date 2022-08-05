package com.pyrinnl.githubrepo.model

import com.pyrinnl.githubrepo.data.retrofit.RetrofitRepoSource
import com.pyrinnl.githubrepo.model.entities.Repo
import com.pyrinnl.githubrepo.model.entities.RepoDetails
import com.pyrinnl.githubrepo.model.entities.UserInfo
import com.pyrinnl.githubrepo.model.settings.AppSettings
import com.pyrinnl.githubrepo.utills.isASCII
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val appSettings: AppSettings,
    private val retrofitRepoSource: RetrofitRepoSource
) {

    private var currentRepoName = MutableSharedFlow<String>(
        replay = 1,
        extraBufferCapacity = 0,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    private val currentUserInfo by lazy {
        CoroutineScope(Dispatchers.IO).async { doGetUserInfo() }
    }

    fun isSignIn(): Boolean {
        return appSettings.geCurrentToken() != null
    }

    suspend fun signIn(token: String): UserInfo {
        if (token.isBlank()) throw EmptyFieldException(Field.Token)
        if (!token.isASCII()) throw InvalidInputException()

        val userInfo = try {
            retrofitRepoSource.signIn(token)
        } catch (e: Exception) {
            if (e is BackendException && e.code == 401)
                throw InvalidCredentialsException(e)
            else
                throw e
        }

        appSettings.setCurrentToken(token)
        return userInfo
    }

    suspend fun getRepositories(): List<Repo> {
        val repos = retrofitRepoSource.getRepositories()
        if (repos.isEmpty()) throw EmptyContentException()
        return repos
    }

    suspend fun getRepository(repoName: String): RepoDetails {
        val ownerName = "icerockdev"
        return retrofitRepoSource.getRepository(/*currentUserInfo.await().ownerName*/ownerName, repoName)
    }

    suspend fun getRepositoryReadme(repositoryName: String) = wrapBackendExceptions {
        try {
            retrofitRepoSource.getRepositoryReadme(currentUserInfo.await().ownerName, repositoryName)
                .mapToReadme()
        } catch (e: BackendException) {
            if (e.code == 404) throw EmptyContentException()
            else throw e
        }
    }

    fun listenCurrentRepoName(): Flow<String> = currentRepoName

    suspend fun updateCurrentRepoName(repoName: String) {
        currentRepoName.emit(repoName)
    }

    private suspend fun doGetUserInfo() = wrapBackendExceptions {
        try {
            retrofitRepoSource.getUserInfo()
        } catch (e: BackendException) {
            if (e.code == 404) throw AuthException(e)
            else throw e
        }
    }

    fun logout() {
        appSettings.setCurrentToken(null)
    }
}


