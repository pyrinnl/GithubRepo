package com.pyrinnl.githubrepo.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.pyrinnl.githubrepo.Const
import com.pyrinnl.githubrepo.data.retrofit.RepoApi
import com.pyrinnl.githubrepo.model.settings.AppSettings
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {


    @Provides
    @Singleton
    fun provideRepoApi(retrofit: Retrofit): RepoApi {
        return retrofit.create(RepoApi::class.java)
    }

    @Provides
    @Singleton
    fun provideClient(settings: AppSettings): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(createAuthorizationInterceptor(settings))
            .addInterceptor(createLoggingInterceptor())
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val contentType = "application/vnd.github+json".toMediaType()
        val json = Json {ignoreUnknownKeys = true}
        return Retrofit.Builder()
            .baseUrl(Const.BASE_URL)
            .addConverterFactory(json.asConverterFactory(contentType))
            .client(client)
            .build()
    }

    private fun createAuthorizationInterceptor(settings: AppSettings): Interceptor {
        return Interceptor { chain ->
            val newBuilder = chain.request().newBuilder()
            val token = /*settings.geCurrentToken()*/ null
            if (token != null) {
                newBuilder.addHeader("Authorization", "token $token")
            }
            return@Interceptor chain.proceed(newBuilder.build())
        }
    }

    private fun createLoggingInterceptor(): Interceptor {
        return HttpLoggingInterceptor()
            .setLevel(HttpLoggingInterceptor.Level.BODY)
    }
}