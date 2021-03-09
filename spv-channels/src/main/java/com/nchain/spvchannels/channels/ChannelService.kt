package com.nchain.spvchannels.channels

import com.nchain.spvchannels.channels.models.ChannelResponse
import com.nchain.spvchannels.channels.models.create.CreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChannelService {
    @POST("/api/v1/account/{accountId}/channel")
    suspend fun createChannel(
        @Path("accountId") accountId: String,
        @Body request: CreateRequest,
    ): Response<ChannelResponse>

    @GET("/api/v1/account/{accountId}/channel/{channelId}")
    suspend fun getChannel(
        @Path("accountId") accountId: String,
        @Path("channelId") channelId: String,
    ): Response<ChannelResponse>
}
