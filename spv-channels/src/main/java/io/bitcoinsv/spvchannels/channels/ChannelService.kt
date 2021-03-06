// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.channels

import io.bitcoinsv.spvchannels.channels.models.ChannelInfo
import io.bitcoinsv.spvchannels.channels.models.ChannelPermissions
import io.bitcoinsv.spvchannels.channels.models.TokenInfo
import io.bitcoinsv.spvchannels.channels.models.api.ChannelsResponse
import io.bitcoinsv.spvchannels.channels.models.api.CreateRequest
import io.bitcoinsv.spvchannels.channels.models.api.CreateTokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

internal interface ChannelService {
    @GET("/api/v1/account/{accountId}/channel/list")
    suspend fun getAllChannels(
        @Path("accountId") accountId: String,
    ): Response<ChannelsResponse>

    @POST("/api/v1/account/{accountId}/channel")
    suspend fun createChannel(
        @Path("accountId") accountId: String,
        @Body request: CreateRequest,
    ): Response<ChannelInfo>

    @POST("/api/v1/account/{accountId}/channel/{channelId}")
    suspend fun amendChannel(
        @Path("accountId") accountId: String,
        @Path("channelId") channelId: String,
        @Body channelPermissions: ChannelPermissions,
    ): Response<ChannelPermissions>

    @GET("/api/v1/account/{accountId}/channel/{channelId}")
    suspend fun getChannel(
        @Path("accountId") accountId: String,
        @Path("channelId") channelId: String,
    ): Response<ChannelInfo>

    @DELETE("/api/v1/account/{accountId}/channel/{channelId}")
    suspend fun deleteChannel(
        @Path("accountId") accountId: String,
        @Path("channelId") channelId: String,
    ): Response<Unit>

    @GET("/api/v1/account/{accountId}/channel/{channelId}/api-token")
    suspend fun getTokens(
        @Path("accountId") accountId: String,
        @Path("channelId") channelId: String,
        @Query("token") token: String? = null
    ): Response<List<TokenInfo>>

    @GET("/api/v1/account/{accountId}/channel/{channelId}/api-token/{tokenId}")
    suspend fun getTokenInfo(
        @Path("accountId") accountId: String,
        @Path("channelId") channelId: String,
        @Path("tokenId") tokenId: String,
    ): Response<TokenInfo>

    @POST("/api/v1/account/{accountId}/channel/{channelId}/api-token")
    suspend fun createToken(
        @Path("accountId") accountId: String,
        @Path("channelId") channelId: String,
        @Body request: CreateTokenRequest
    ): Response<TokenInfo>

    @DELETE("/api/v1/account/{accountId}/channel/{channelId}/api-token/{tokenId}")
    suspend fun revokeToken(
        @Path("accountId") accountId: String,
        @Path("channelId") channelId: String,
        @Path("tokenId") tokenId: String,
    ): Response<Unit>
}
