package com.pyrinnl.githubrepo.data.retrofit

import com.pyrinnl.githubrepo.Const
import com.pyrinnl.githubrepo.data.retrofit.base.BaseRetrofitSource
import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoDetailsResponseEntity
import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoReadmeResponseEntity
import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoResponseEntity
import com.pyrinnl.githubrepo.data.retrofit.entities.SignInResponseEntity
import com.pyrinnl.githubrepo.model.RepoSource
import kotlinx.coroutines.delay
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RetrofitRepoSource @Inject constructor(
    private val repoApi: RepoApi,
) : BaseRetrofitSource(), RepoSource {


    override suspend fun signIn(token: String): SignInResponseEntity = wrapRetrofitExceptions {
        delay(2000)
        repoApi.signIn("${Const.START_POINT} $token")
    }

    override suspend fun getRepositories(): List<GetRepoResponseEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun getRepository(repoId: String): GetRepoDetailsResponseEntity {
        TODO("Not yet implemented")
    }

    override suspend fun getRepositoryReadme(
        ownerName: String,
        repositoryName: String
    ): GetRepoReadmeResponseEntity {
        TODO("Not yet implemented")
    }
}