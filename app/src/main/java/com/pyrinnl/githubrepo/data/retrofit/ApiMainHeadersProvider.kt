package com.pyrinnl.githubrepo.data.retrofit


class ApiMainHeadersProvider {


    fun getAuthenticatedHeaders(accessToken: String): AuthenticatedHeaders =
        AuthenticatedHeaders().also {
            it[AUTHORIZATION] = getBearer(accessToken)
        }


    companion object {
        private const val AUTHORIZATION = "Authorization"
        private fun getBearer(accessToken: String) = "token $accessToken"
    }
}

class AuthenticatedHeaders : MainApiHeaders<String, String>()
open class MainApiHeaders<T, U> : HashMap<String, String>()



