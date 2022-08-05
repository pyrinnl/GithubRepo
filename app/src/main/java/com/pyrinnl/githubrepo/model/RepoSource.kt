package com.pyrinnl.githubrepo.model

import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoReadmeResponseEntity
import com.pyrinnl.githubrepo.model.entities.Repo
import com.pyrinnl.githubrepo.model.entities.RepoDetails
import com.pyrinnl.githubrepo.model.entities.UserInfo

interface RepoSource {

    suspend fun signIn(token: String): UserInfo

    suspend fun getUserInfo(): UserInfo

    suspend fun getRepositories(): List<Repo>

    suspend fun getRepository(ownerName: String, repoName: String): RepoDetails

    suspend fun getRepositoryReadme(ownerName: String, repoName: String): GetRepoReadmeResponseEntity
}