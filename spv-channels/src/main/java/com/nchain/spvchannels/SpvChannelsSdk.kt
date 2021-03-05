package com.nchain.spvchannels

import com.nchain.spvchannels.channels.BasicAuthInterceptor
import com.nchain.spvchannels.channels.Channel
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create

/**
 * SPV Channels main entry point. Provides access to Channels and Messaging APIs.
 *
 * @param baseUrl the base url of the SPV channels server
 */
class SpvChannelsSdk(private val baseUrl: String) {
    /**
     * Creates and returns a new [Channel] object with given credentials.
     *
     * @param username the username to use with channel authentication
     * @param password the password to use with channel authentication
     */
    fun channelWithCredentials(username: String, password: String): Channel {
        val client = createClient(BasicAuthInterceptor(username, password))
        val retrofit = createRetrofit(client)

        return Channel(retrofit.create())
    }

    /**
     * Create a [Retrofit] instance to execute calls to the SPV APIs.
     *
     * @param client the client to use with Retrofit
     */
    private fun createRetrofit(client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .client(client)
            .baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
    }

    /**
     * Create and configure [OkHttpClient] to use for communication with required interceptors.
     *
     * @param interceptors the interceptors to use in client
     */
    private fun createClient(vararg interceptors: Interceptor): OkHttpClient {
        return OkHttpClient.Builder().apply {
            if (BuildConfig.DEBUG) {
                addInterceptor(createLoggingInterceptor())
            }
            interceptors.forEach { addInterceptor(it) }
        }.build()
    }

    /**
     * Create a logging [Interceptor] to log requests and responses
     */
    private fun createLoggingInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }
}
