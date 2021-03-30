package io.bitcoinsv.spvchannels.channels

import android.text.TextUtils
import okhttp3.Credentials
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Creates an interceptor for basic auth.
 *
 * @param username the username to use for basic auth
 * @param password the password to use for basic auth
 *
 * @throws IllegalArgumentException if either username or password is an empty string
 */
internal class BasicAuthInterceptor(username: String, password: String) : Interceptor {
    private val credentials: String

    init {
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            throw IllegalArgumentException("Username and password cannot be empty")
        }
        credentials = Credentials.basic(username, password)
    }

    /**
     * Intercept the request and add Authorization header with basic credentials.
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
