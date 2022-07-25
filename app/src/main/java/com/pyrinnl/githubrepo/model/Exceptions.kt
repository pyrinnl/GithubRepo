package com.pyrinnl.githubrepo.model


open class AppException : RuntimeException {
    constructor() : super()
    constructor(message: String) : super(message)
    constructor(cause: Throwable) : super(cause)
}

class EmptyFieldException(
    val field: Field
) : AppException()

class InvalidInputException : AppException()

class AutException(
    cause: Throwable
) : AppException(cause = cause)

class InvalidCredentialsException(
    cause: Throwable
) : AppException(cause = cause)

class ConnectionException(cause: Throwable) : AppException(cause = cause)

class BackendException(
    val code: Int,
    message: String
) : AppException(message)

class ParseBackendResponseException(
    cause: Throwable
) : AppException(cause = cause)

internal fun <T> wrapBackendException(block: () -> T): T {
    try {
        return block()
    } catch (e: BackendException) {
        if (e.code == 401) {
            throw AutException(e)
        } else
            throw e
    }
}
