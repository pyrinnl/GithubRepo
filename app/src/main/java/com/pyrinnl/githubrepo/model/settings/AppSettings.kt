package com.pyrinnl.githubrepo.model.settings

interface AppSettings {

    fun geCurrentToken(): String?
    
    fun setCurrentToken(token: String?)
}