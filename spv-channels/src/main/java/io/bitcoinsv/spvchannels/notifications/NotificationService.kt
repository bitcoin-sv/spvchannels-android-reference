// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.notifications

import io.bitcoinsv.spvchannels.notifications.models.TokenRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

interface NotificationService {
    @POST("/api/v1/pushnotifications")
    suspend fun registerFcmToken(
        @Header("Authorization") apiToken: String,
        @Body request: TokenRequest,
    ): Response<Void>

    @PUT("/api/v1/pushnotifications/{oldtoken}")
    suspend fun updateToken(
        @Path("oldtoken") oldToken: String,
        @Body request: TokenRequest,
    ): Response<Void>

    @DELETE("/api/v1/pushnotifications/{oldtoken}")
    suspend fun deleteToken(
        @Path("oldtoken") oldToken: String,
        @Query("channelid") channelId: String? = null,
    ): Response<Void>
}
