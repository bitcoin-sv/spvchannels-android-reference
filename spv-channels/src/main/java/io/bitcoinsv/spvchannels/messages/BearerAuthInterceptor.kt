
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.messages

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Creates an interceptor for Bearer token authentication.
 *
 * @param token the bearer token to use
 */
internal class BearerAuthInterceptor(token: String) : Interceptor {
    private val credentials = "Bearer $token"

    /**
     * Intercept the request and add Authorization header with bearer token.
     *
     * @param chain the chain to inject authentication header into
     */
    override fun intercept(chain: Interceptor.Chain): Response {
        val authenticatedRequest = chain.request()
            .newBuilder()
            .header("Authorization", credentials)
            .build()

        return chain.proceed(authenticatedRequest)
    }
}
