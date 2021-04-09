// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.response

import java.net.HttpURLConnection
import retrofit2.Response

/**
 * The status of the request.
 */
sealed class Status<out T> {
    /**
     * If the request was successful (and contains no body), this instance will be returned,
     * containing the [value], depending on the endpoint that was used.
     */
    data class Success<T>(val value: T) : Status<T>()

    /**
     * If the server returned no content. Some endpoints expect no return body.
     */
    object NoContent : Status<Nothing>()

    /**
     * The credentials were not provided.
     */
    object Unauthorized : Status<Nothing>()

    /**
     * The credentials were not valid.
     */
    object Forbidden : Status<Nothing>()

    /**
     * The object that was requested could not be found. Likely an issue with empty or invalid
     * accountId, channelId, etc.
     */
    object NotFound : Status<Nothing>()

    /**
     * The server returned an error that we did not expect.
     */
    object ServerError : Status<Nothing>()

    companion object {
        /**
         * Maps the response from Retrofit to a Status object.
         */
        internal fun <T> fromResponse(response: Response<T>): Status<T> {
            return fromResponse(response) { it }
        }

        /**
         * Maps the response from Retrofit to a Status object, using the provided mapping function.
         */
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
