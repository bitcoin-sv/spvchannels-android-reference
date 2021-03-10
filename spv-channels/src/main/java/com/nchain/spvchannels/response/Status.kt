package com.nchain.spvchannels.response

import java.net.HttpURLConnection
import retrofit2.Response

sealed class Status<out T> {
    data class Success<T>(val value: T) : Status<T>()
    object NoContent : Status<Nothing>()
    object Unauthorized : Status<Nothing>()
    object Forbidden : Status<Nothing>()
    object NotFound : Status<Nothing>()
    object ServerError : Status<Nothing>()

    companion object {
        internal fun <T> fromResponse(response: Response<T>): Status<T> {
            return fromResponse(response) { it }
        }

        internal fun <T, R> fromResponse(
            response: Response<T>,
            mapper: (T) -> R
        ): Status<R> {
            if (response.isSuccessful) {
                val body = response.body() ?: return NoContent
                return Success(mapper(body))
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
