package com.nchain.spvchannels.response

import java.net.HttpURLConnection
import retrofit2.Response

sealed class Status<out T> {
    data class Success<T>(val value: T) : Status<T>()
    object Unauthorized : Status<Nothing>()
    object Forbidden : Status<Nothing>()
    object NotFound : Status<Nothing>()
    object ServerError : Status<Nothing>()

    companion object {
        fun <T> fromResponse(response: Response<T>): Status<T> {
            if (response.isSuccessful) {
                val body = response.body() ?: return NotFound
                return Success(body)
            }

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
