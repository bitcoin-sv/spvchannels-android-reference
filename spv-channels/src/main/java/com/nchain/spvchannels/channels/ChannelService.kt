package com.nchain.spvchannels.channels

import com.nchain.spvchannels.channels.models.create.CreateRequest
import com.nchain.spvchannels.channels.models.create.CreateResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

interface ChannelService {
    @POST("/api/v1/account/{accountId}/channel")
    suspend fun createChannel(
        @Path("accountId") accountId: String,
        @Body request: CreateRequest
    ): Response<CreateResponse>
}
