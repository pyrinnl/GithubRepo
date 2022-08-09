package com.pyrinnl.githubrepo.di

import com.pyrinnl.githubrepo.data.retrofit.RetrofitRepoSource
import com.pyrinnl.githubrepo.data.retrofit.RepoSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class SourcesModule {

    @Binds
    abstract fun bindRepoSource(
        retrofitRepoSource: RetrofitRepoSource
    ): RepoSource
}