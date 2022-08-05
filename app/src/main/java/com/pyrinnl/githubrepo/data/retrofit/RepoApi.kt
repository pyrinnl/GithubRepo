package com.pyrinnl.githubrepo.data.retrofit

import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoReadmeResponseEntity
import com.pyrinnl.githubrepo.data.retrofit.entities.GetRepoResponseEntity
import com.pyrinnl.githubrepo.data.retrofit.entities.SignInResponseEntity
import retrofit2.http.*


interface RepoApi {

    @GET("/user")
    suspend fun signIn(@Header("Authorization") token: String): SignInResponseEntity

    @GET("/user")
    suspend fun getUserInfo(): SignInResponseEntity

    @GET("/user/repos")
    suspend fun getRepositories(): List<GetRepoResponseEntity>


    @GET("/repos/{owner}/{repo}")
    suspend fun getRepository(
        @Path("owner") owner: String,
        @Path("repo") repositoryName: String
    ): GetRepoResponseEntity

    @GET("/repos/{owner}/{repo}/readme")
    suspend fun getRepositoryReadme(
        @Path("owner") owner: String,
        @Path("repo") repositoryName: String
    ): GetRepoReadmeResponseEntity
}