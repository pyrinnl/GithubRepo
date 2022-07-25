package com.pyrinnl.githubrepo.logger

import android.util.Log

object LogCatLogger: Logger {


    override fun log(tag: String, message: String) {
        Log.d(tag, message)
    }

    override fun error(tag: String, e: Throwable) {
        Log.d(tag, "Error!", e)
    }
}