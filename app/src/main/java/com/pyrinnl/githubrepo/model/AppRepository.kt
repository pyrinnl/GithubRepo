package com.pyrinnl.githubrepo.model

import com.pyrinnl.githubrepo.Const
import com.pyrinnl.githubrepo.data.retrofit.RetrofitRepoSource
import com.pyrinnl.githubrepo.data.retrofit.entities.SignInResponseEntity
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
    private val retrofitRepoSource: RetrofitRepoSource
) {

    fun isSignedIn(): Boolean {
        return appSettings.geCurrentToken() != null
    }

    suspend fun signIn(token: String): UserInfo {
        if (token.isBlank()) throw EmptyFieldException(Field.Token)
        if(!token.isASCII()) throw InvalidInputException()

       val response: SignInResponseEntity =  try {
            retrofitRepoSource.signIn(token)
        } catch (e: Exception) {
            if (e is BackendException && e.code == 401)
                throw InvalidCredentialsException(e)
            else
                throw e
        }

        appSettings.setCurrentToken(token)
        return response.mapToUserInfo()
    }

    suspend fun getRepositories(): List<Repo> {
        TODO()
    }

    suspend fun getRepository(repoId: String): RepoDetails {
        TODO()
    }

    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String): String {
        TODO()
    }


    fun logout() {
        appSettings.setCurrentToken(null)
    }
}