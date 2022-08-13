package com.pyrinnl.githubrepo.model

import com.pyrinnl.githubrepo.model.entities.Repo
import com.pyrinnl.githubrepo.model.entities.RepoDetails
import com.pyrinnl.githubrepo.model.entities.UserInfo
import com.pyrinnl.githubrepo.model.settings.AppSettings
import com.pyrinnl.githubrepo.utills.isASCII
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class AppRepository @Inject constructor(
    private val appSettings: AppSettings,
    private val retrofitRepoSource: RepoSource
) {

    private lateinit var currentUserInfo: UserInfo

    fun isSignIn(): Boolean {
        return appSettings.geCurrentToken() != null
    }

    suspend fun signIn(token: String) {
        if (token.isBlank()) throw EmptyFieldException()
        if (!token.isASCII()) throw InvalidInputException()

        currentUserInfo = try {
            retrofitRepoSource.signIn(token)
        } catch (e: Exception) {
            if (e is BackendException && e.code == 401)
                throw InvalidCredentialsException(e)
            else
                throw e
        }
        appSettings.setCurrentToken(token)
    }

    suspend fun getRepositories(): List<Repo> {
        val repos = retrofitRepoSource.getRepositories()
        if (repos.isEmpty()) throw EmptyContentException()
        return repos
    }

    suspend fun getRepository(repoName: String): RepoDetails {
        return retrofitRepoSource.getRepository(currentUserInfo.ownerName, repoName)
    }

    suspend fun getRepositoryReadme(repositoryName: String) = wrapBackendExceptions {
        try {
            retrofitRepoSource.getRepositoryReadme(currentUserInfo.ownerName, repositoryName)
                .mapToReadme()
        } catch (e: BackendException) {
            if (e.code == 404) throw EmptyContentException()
            else throw e
        }
    }


    suspend fun doGetUserInfo() = wrapBackendExceptions {
        try {
            currentUserInfo = retrofitRepoSource.getUserInfo()
        } catch (e: BackendException) {
            if (e.code == 404) throw AuthException(e)
            else throw e
        }
    }

    fun logout() {
        appSettings.setCurrentToken(null)
    }
}