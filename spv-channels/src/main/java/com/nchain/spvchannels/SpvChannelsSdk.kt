package com.nchain.spvchannels

import com.nchain.spvchannels.channels.BasicAuthInterceptor
import com.nchain.spvchannels.channels.Channel
import com.nchain.spvchannels.datetime.IsoDateTimeConverter
import com.nchain.spvchannels.messages.BearerAuthInterceptor
import com.nchain.spvchannels.messages.Messaging
import com.squareup.moshi.Moshi
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.Dispatchers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Converter
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
     * @param accountId the ID of the account for which the channels should be managed
     * @param username the username to use with channel authentication
     * @param password the password to use with channel authentication
     * @param coroutineContext the coroutine context to use for requests, Dispatcher.IO is default
     * and should provide desired functionality, should be replaced with your test dispatcher in
     * tests.
     */
    fun channelWithCredentials(
        accountId: String,
        username: String,
        password: String,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Channel {
        val client = createClient(BasicAuthInterceptor(username, password))
        val retrofit = createRetrofit(client)

        return Channel(retrofit.create(), accountId, coroutineContext)
    }

    /**
     * Creates and returns a new [Messaging] object for a given channelId, authorized by token.
     *
     * @param channelId the id of the channel to create a messaging object for
     * @param token the token to use for authorization
     * @param coroutineContext the coroutine context to use for the requests, Dispatchers.IO is
     * the default and should provide the desired functionality, should be replaced by your test
     * dispatcher in unit tests.
     */
    fun messagingWithToken(
        channelId: String,
        token: String,
        coroutineContext: CoroutineContext = Dispatchers.IO
    ): Messaging {
        val client = createClient(BearerAuthInterceptor(token))
        val retrofit = createRetrofit(client)

        return Messaging(retrofit.create(), channelId, coroutineContext)
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
            .addConverterFactory(createMoshi())
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

    /**
     * Creates a [Converter.Factory] for serialization using Moshi
     */
    private fun createMoshi(): Converter.Factory {
        val moshi = Moshi.Builder()
            .add(IsoDateTimeConverter())
            .build()
        return MoshiConverterFactory.create(moshi)
    }
}
