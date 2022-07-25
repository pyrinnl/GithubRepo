package com.pyrinnl.githubrepo.logger

interface Logger {

    fun log(tag: String, message: String)

    fun error(tag: String, e: Throwable)
}