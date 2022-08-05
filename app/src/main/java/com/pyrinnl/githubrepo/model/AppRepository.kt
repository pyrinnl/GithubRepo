package com.pyrinnl.githubrepo.model

import com.pyrinnl.githubrepo.data.retrofit.RetrofitRepoSource
import com.pyrinnl.githubrepo.model.entities.Readme
import com.pyrinnl.githubrepo.model.entities.Repo
import com.pyrinnl.githubrepo.model.entities.RepoDetails
import com.pyrinnl.githubrepo.model.entities.UserInfo
import com.pyrinnl.githubrepo.model.settings.AppSettings
import com.pyrinnl.githubrepo.utills.isASCII
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val appSettings: AppSettings,
    private val retrofitRepoSource: RetrofitRepoSource
) {

    val user: UserInfo? = null

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
        return retrofitRepoSource.getRepositories()
    }

    suspend fun getRepository(repoName: String): RepoDetails {
        val userInfo = doGetUserInfo()
        return retrofitRepoSource.getRepository(userInfo.ownerName, repoName)
    }

    suspend fun getRepositoryReadme( repositoryName: String): Readme {
        val userInfo = doGetUserInfo()
       return retrofitRepoSource.getRepositoryReadme(userInfo.ownerName, repositoryName)
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

