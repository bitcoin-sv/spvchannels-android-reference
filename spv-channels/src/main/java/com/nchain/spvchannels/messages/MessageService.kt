package com.nchain.spvchannels.messages

import com.nchain.spvchannels.messages.models.Message
import okhttp3.MediaType
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HEAD
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface MessageService {
    @HEAD("/api/v1/channel/{channelId}")
    suspend fun getMaxSequence(
        @Path("channelId") channelId: String,
    ): Response<Void>

    @POST("/api/v1/channel/{channelId}")
    suspend fun sendMessage(
        @Path("channelId") channelId: String,
        @Header("Content-Type") contentType: MediaType,
        @Body message: ByteArray
    ): Response<Message>

    @GET("/api/v1/channel/{channelId}")
    suspend fun getMessages(
        @Path("channelId") channelId: String,
        @Query("unread") unread: Boolean?
    ): Response<List<Message>>
}
