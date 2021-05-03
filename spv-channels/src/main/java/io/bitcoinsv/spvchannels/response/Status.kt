// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.response

import java.net.HttpURLConnection
import okhttp3.ResponseBody
import retrofit2.Converter
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
     * Returned when invalid data was sent to the server. Contains [error] to describe the reason
     * for failure.
     */
    data class InvalidRequest(val error: String) : Status<Nothing>()

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
     * The Channel was created as sequenced, and the API token associated with the request has not
     * marked as read the latest messages in the stream. The client must catch up then, if it is
     * still appropriate, retry the write attempt.
     */
    object Conflict : Status<Nothing>()

    /**
     * The server returned an error that we did not expect.
     */
    object ServerError : Status<Nothing>()

    companion object {
        /**
         * Maps the response from Retrofit to a Status object.
         */
        internal fun <T> fromResponse(
            converter: Converter<ResponseBody, ChannelsError>,
            response: Response<T>
        ): Status<T> {
            return fromResponse(converter, response) { it }
        }

        /**
         * Maps the response from Retrofit to a Status object, using the provided mapping function.
         */
        internal fun <T, R> fromResponse(
            converter: Converter<ResponseBody, ChannelsError>,
            response: Response<T>,
            mapper: (T) -> R
        ): Status<R> {
            if (response.isSuccessful) {
                val body = response.body() ?: return NoContent
                return Success(mapper(body))
            }

            return when (response.code()) {
                HttpURLConnection.HTTP_BAD_REQUEST -> parseBadRequest(converter, response)
                HttpURLConnection.HTTP_UNAUTHORIZED -> Unauthorized
                HttpURLConnection.HTTP_FORBIDDEN -> Forbidden
                HttpURLConnection.HTTP_NOT_FOUND -> NotFound
                HttpURLConnection.HTTP_INTERNAL_ERROR -> ServerError
                HttpURLConnection.HTTP_CONFLICT -> Conflict
                else -> ServerError
            }
        }

        private fun <T, R> parseBadRequest(
            converter: Converter<ResponseBody, ChannelsError>,
            response: Response<T>
        ): Status<R> {
            val errorBody = response.errorBody() ?: return ServerError
            val error = converter.convert(errorBody) ?: return ServerError

            return InvalidRequest(error.title)
        }
    }
}
