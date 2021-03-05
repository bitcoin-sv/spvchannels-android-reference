package com.nchain.spvchannels.response

import java.net.HttpURLConnection
import retrofit2.Response

sealed class Result {
    data class Success<T>(val value: T) : Result()
    object Unauthorized : Result()
    object Forbidden : Result()
    object NotFound : Result()
    object ServerError : Result()

    companion object {
        fun <T> fromResponse(response: Response<T>): Result {
            if (response.isSuccessful) return Success(response.body())

            return when (response.code()) {
                HttpURLConnection.HTTP_UNAUTHORIZED -> Unauthorized
                HttpURLConnection.HTTP_FORBIDDEN -> Forbidden
                HttpURLConnection.HTTP_NOT_FOUND -> NotFound
                HttpURLConnection.HTTP_INTERNAL_ERROR -> ServerError
                else -> ServerError
            }
        }
    }
}
