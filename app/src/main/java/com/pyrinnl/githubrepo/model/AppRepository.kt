package com.pyrinnl.githubrepo.model

import com.pyrinnl.githubrepo.model.entities.Repo
import com.pyrinnl.githubrepo.model.entities.RepoDetails
import com.pyrinnl.githubrepo.model.entities.UserInfo

class AppRepository {
    suspend fun getRepositories(): List<Repo> {
        TODO()
    }

    suspend fun getRepository(repoId: String): RepoDetails {
        TODO()
    }

    suspend fun getRepositoryReadme(ownerName: String, repositoryName: String, branchName: String): String {
        TODO()
    }

    suspend fun signIn(token: String): UserInfo {
        TODO()
    }

    // TODO:
}