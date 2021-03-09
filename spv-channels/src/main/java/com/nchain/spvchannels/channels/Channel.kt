package com.nchain.spvchannels.channels

import com.nchain.spvchannels.channels.models.ChannelInfo
import com.nchain.spvchannels.channels.models.ChannelPermissions
import com.nchain.spvchannels.channels.models.Retention
import com.nchain.spvchannels.channels.models.api.CreateRequest
import com.nchain.spvchannels.response.Status
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

class Channel(
    private val service: ChannelService,
    private val accountId: String,
    private val context: CoroutineContext
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
}
