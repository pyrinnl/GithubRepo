package com.pyrinnl.githubrepo.data.retrofit

import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoResponseEntity
import com.pyrinnl.githubrepo.data.retrofit.entities.SignInResponseEntity
import retrofit2.http.*


interface RepoApi {

    @GET("/user")
    suspend fun signIn(@Header("Authorization") token: String): SignInResponseEntity


    @GET("/user/repos")
    fun getRepositories(): List<GetRepoResponseEntity>

    @GET("/user/{owner}/{repo}")
    fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repositoryName: String
    ): GetRepoResponseEntity

    fun getRepositoryReadme()
}