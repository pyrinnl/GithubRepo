package com.pyrinnl.githubrepo.data.retrofit

import com.pyrinnl.githubrepo.Const
import com.pyrinnl.githubrepo.data.retrofit.base.BaseRetrofitSource
import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoReadmeResponseEntity
import com.pyrinnl.githubrepo.model.entities.Repo
import com.pyrinnl.githubrepo.model.entities.RepoDetails
import com.pyrinnl.githubrepo.model.entities.UserInfo
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitRepoSource @Inject constructor(
    private val repoApi: RepoApi,
) : BaseRetrofitSource(), RepoSource {


    override suspend fun signIn(token: String): UserInfo = wrapRetrofitExceptions {
        repoApi.signIn("${Const.START_POINT} $token").mapToUserInfo()
    }

    override suspend fun getUserInfo(): UserInfo = wrapRetrofitExceptions {
        repoApi.getUserInfo().mapToUserInfo()
    }

    override suspend fun getRepositories(): List<Repo> = wrapRetrofitExceptions {
        repoApi.getRepositories().map {
            it.mapToRepo()
        }
    }

    override suspend fun getRepository(ownerName: String, repoName: String): RepoDetails =
        wrapRetrofitExceptions {
            repoApi.getRepository(ownerName, repoName).mapToRepoDetails()
        }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repoName: String
    ): GetRepoReadmeResponseEntity = wrapRetrofitExceptions {
        repoApi.getRepositoryReadme(ownerName, repoName)
    }
}