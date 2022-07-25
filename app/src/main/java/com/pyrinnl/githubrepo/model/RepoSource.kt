package com.pyrinnl.githubrepo.model

import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoDetailsResponseEntity
import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoReadmeResponseEntity
import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoResponseEntity
import com.pyrinnl.githubrepo.data.retrofit.entities.SignInResponseEntity

interface RepoSource {

    suspend fun signIn(token: String): SignInResponseEntity

    suspend fun getRepositories(): List<GetRepoResponseEntity>

    suspend fun getRepository(repoId: String): GetRepoDetailsResponseEntity

    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String): GetRepoReadmeResponseEntity
}