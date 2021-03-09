package com.nchain.spvchannels.channels

import com.nchain.spvchannels.channels.models.Channel
import com.nchain.spvchannels.channels.models.api.ChannelsResponse
import com.nchain.spvchannels.channels.models.api.CreateRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ChannelService {
    @GET("/api/v1/account/{accountId}/channel/list")
    suspend fun getAllChannels(
        @Path("accountId") accountId: String,
    ): Response<ChannelsResponse>

    @POST("/api/v1/account/{accountId}/channel")
    suspend fun createChannel(
        @Path("accountId") accountId: String,
        @Body request: CreateRequest,
    ): Response<Channel>

    @GET("/api/v1/account/{accountId}/channel/{channelId}")
    suspend fun getChannel(
        @Path("accountId") accountId: String,
        @Path("channelId") channelId: String,
    ): Response<Channel>
}
