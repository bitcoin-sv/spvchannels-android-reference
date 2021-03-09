package com.nchain.spvchannels.channels

import com.nchain.spvchannels.channels.models.Channel
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
    suspend fun getAllChannels(): Status<List<Channel>> = withContext(context) {
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
    ): Status<Channel> = withContext(context) {
        Status.fromResponse(
            service.createChannel(
                accountId,
                CreateRequest(publicRead, publicWrite, sequenced, retention)
            )
        )
    }

    suspend fun getChannel(channelId: String): Status<Channel> = withContext(context) {
        Status.fromResponse(
            service.getChannel(accountId, channelId)
        )
    }
}
