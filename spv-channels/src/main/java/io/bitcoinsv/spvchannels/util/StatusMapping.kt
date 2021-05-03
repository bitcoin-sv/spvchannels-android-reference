// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.util

import io.bitcoinsv.spvchannels.response.ChannelsError
import io.bitcoinsv.spvchannels.response.Status
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Response

internal interface StatusMapping {
    val errorConverter: Converter<ResponseBody, ChannelsError>

    /**
     * Maps response to status, converting the body to a different type.
     *
     * @param mapper the function to use to map the original response
     * @param call the call to perform
     */
    suspend fun <T, U> mapFromResponse(
        mapper: (T) -> U,
        call: suspend () -> Response<T>
    ): Status<U> {
        return Status.fromResponse(errorConverter, call(), mapper)
    }

    /**
     * Map response to a status.
     *
     * @param call the call to perform
     */
    suspend fun <T> fromResponse(
        call: suspend () -> Response<T>
    ): Status<T> {
        return Status.fromResponse(errorConverter, call())
    }
}
