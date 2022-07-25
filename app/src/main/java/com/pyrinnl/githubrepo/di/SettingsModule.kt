package com.pyrinnl.githubrepo.di

import com.pyrinnl.githubrepo.model.settings.AppSettings
import com.pyrinnl.githubrepo.model.settings.KeyValueStorage
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class SettingsModule {

    @Binds
    abstract fun bindAppSettings(
        appSettings: KeyValueStorage
    ): AppSettings
}