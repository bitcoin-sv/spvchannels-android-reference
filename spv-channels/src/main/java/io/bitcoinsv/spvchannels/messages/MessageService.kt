
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.messages

import io.bitcoinsv.spvchannels.messages.models.Message
import io.bitcoinsv.spvchannels.messages.models.ReadRequest
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.HEAD
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
        @Body message: RequestBody
    ): Response<Message>

    @GET("/api/v1/channel/{channelId}")
    suspend fun getMessages(
        @Path("channelId") channelId: String,
        @Query("unread") unread: Boolean?,
    ): Response<List<Message>>

    @POST("/api/v1/channel/{channelId}/{sequenceId}")
    suspend fun markMessageRead(
        @Path("channelId") channelId: String,
        @Path("sequenceId") sequenceId: String,
        @Query("older") older: Boolean? = false,
        @Body request: ReadRequest,
    ): Response<Unit>

    @DELETE("/api/v1/channel/{channelId}/{sequenceId}")
    suspend fun deleteMessage(
        @Path("channelId") channelId: String,
        @Path("sequenceId") sequenceId: String,
    ): Response<Unit>
}
