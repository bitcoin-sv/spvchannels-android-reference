package com.nchain.spvchannels.channels

import com.nchain.spvchannels.channels.models.ChannelInfo
import com.nchain.spvchannels.channels.models.ChannelPermissions
import com.nchain.spvchannels.channels.models.Retention
import com.nchain.spvchannels.channels.models.TokenInfo
import com.nchain.spvchannels.channels.models.api.CreateRequest
import com.nchain.spvchannels.channels.models.api.CreateTokenRequest
import com.nchain.spvchannels.response.Status
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

class Channel internal constructor(
    private val service: ChannelService,
    private val accountId: String,
    private val context: CoroutineContext,
) {
    suspend fun getAllChannels(): Status<List<ChannelInfo>> = withContext(context) {
        Status.fromResponse(
            service.getAllChannels(accountId)
        ) {
            it.channels
        }
    }

    suspend fun createChannel(
        publicRead: Boolean,
        publicWrite: Boolean,
        sequenced: Boolean,
        retention: Retention,
    ): Status<ChannelInfo> = withContext(context) {
        Status.fromResponse(
            service.createChannel(
                accountId,
                CreateRequest(publicRead, publicWrite, sequenced, retention)
            )
        )
    }

    suspend fun amendChannel(
        channelId: String,
        publicRead: Boolean,
        publicWrite: Boolean,
        locked: Boolean,
    ): Status<ChannelPermissions> = withContext(context) {
        Status.fromResponse(
            service.amendChannel(
                accountId,
                channelId,
                ChannelPermissions(publicRead, publicWrite, locked)
            )
        )
    }

    suspend fun getChannel(channelId: String): Status<ChannelInfo> = withContext(context) {
        Status.fromResponse(
            service.getChannel(accountId, channelId)
        )
    }

    suspend fun deleteChannel(channelId: String): Status<Unit> = withContext(context) {
        Status.fromResponse(
            service.deleteChannel(accountId, channelId)
        )
    }

    suspend fun getChannelTokens(
        channelId: String,
        token: String? = null
    ): Status<List<TokenInfo>> = withContext(context) {
        Status.fromResponse(
            service.getTokens(accountId, channelId, token)
        )
    }

    suspend fun getTokenInfo(
        channelId: String,
        token: String,
    ): Status<TokenInfo> = withContext(context) {
        Status.fromResponse(
            service.getTokenInfo(accountId, channelId, token)
        )
    }

    suspend fun createToken(
        channelId: String,
        description: String,
        canRead: Boolean,
        canWrite: Boolean,
    ): Status<TokenInfo> = withContext(context) {
        Status.fromResponse(
            service.createToken(
                accountId,
                channelId,
                CreateTokenRequest(description, canRead, canWrite)
            )
        )
    }

    suspend fun revokeToken(
        channelId: String,
        token: String,
    ): Status<Unit> = withContext(context) {
        Status.fromResponse(
            service.revokeToken(
                accountId,
                channelId,
                token
            )
        )
    }
}
