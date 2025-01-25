package com.raji.bookapp.data.remote.network

import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse

sealed interface Result<out D, out E : Error> {

    data class Success<D>(val data: D) : Result<D, Nothing>
    data class NetworkError<out E : Error>(val error: E) : Result<Nothing, E>
}

interface Error
sealed interface DataError : Error {

    enum class RemoteError : DataError {
        REQUEST_TIME_OUT,
        TOO_MANY_REQUESTS,
        NO_INTERNET,
        SERVER,
        SERIALIZATION,
        UNKNOWN
    }

    enum class LocalError : DataError {
        DISK_FULL,
        UNKNOWN
    }
}

inline fun <D, E : Error> Result<D, E>.onSuccess(action: () -> Unit): Result<D, E> {
    return when (this) {
        is Result.Success -> {
            action()
            this
        }

        else -> this
    }
}

inline fun <D, E : Error> Result<D, E>.onError(action: () -> Unit): Result<D, E> {
    return when (this) {
        is Result.NetworkError -> {
            action()
            this
        }

        else -> this
    }
}

inline fun <D, E : Error, R> Result<D, E>.map(map: (D) -> R): Result<R, E> {

    return when (this) {
        is Result.Success ->
            Result.Success(map(this.data))

        is Result.NetworkError -> TODO()
    }
}


suspend inline fun <reified D> HttpResponse.toResult(): Result<D, DataError.RemoteError> {

    return when (status.value) {
        in 200..299 ->
            try {
                Result.Success(body<D>())
            } catch (e: Exception) {
                Result.NetworkError(DataError.RemoteError.SERIALIZATION)
            }


        408 -> Result.NetworkError(DataError.RemoteError.REQUEST_TIME_OUT)
        429 -> Result.NetworkError(DataError.RemoteError.TOO_MANY_REQUESTS)
        in 500..599 -> Result.NetworkError(DataError.RemoteError.SERVER)
        else -> Result.NetworkError(DataError.RemoteError.UNKNOWN)
    }
}