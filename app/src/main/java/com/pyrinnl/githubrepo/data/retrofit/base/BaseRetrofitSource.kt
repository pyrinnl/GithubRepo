package com.pyrinnl.githubrepo.data.retrofit.base

import com.pyrinnl.githubrepo.model.AppException
import com.pyrinnl.githubrepo.model.BackendException
import com.pyrinnl.githubrepo.model.ConnectionException
import com.pyrinnl.githubrepo.model.ParseBackendResponseException
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import org.json.JSONException
import retrofit2.HttpException
import java.io.IOException

open class BaseRetrofitSource {

    suspend fun <T> wrapRetrofitExceptions(block: suspend () -> T): T {
        return try {
            block()
        } catch (e: AppException) {
            throw e
        } catch (e: JSONException) {
            throw ParseBackendResponseException(e)
        } catch (e: HttpException) {
            throw  createBackendException(e)
        } catch (e: IOException) {
            throw ConnectionException(e)
        }
    }

    private fun createBackendException(e: HttpException): Exception {
        return try {
            val json = Json { ignoreUnknownKeys = true }
            val errorBody: ErrorResponseBody =
                json.decodeFromString(
                    e.response()!!.errorBody()!!.string()
                )
            BackendException(e.code(), errorBody.message)
        } catch (e: Exception) {
            throw ParseBackendResponseException(e)
        }
    }

    @Serializable
    class ErrorResponseBody(
        val message: String,
    )
}