package com.nchain.spvchannels.channels

import com.nchain.spvchannels.channels.models.Retention
import com.nchain.spvchannels.channels.models.create.CreateRequest
import com.nchain.spvchannels.channels.models.create.CreateResponse
import com.nchain.spvchannels.response.Status
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

class Channel(
    private val service: ChannelService,
    private val accountId: String,
    private val context: CoroutineContext
) {
    suspend fun createChannel(
        publicRead: Boolean,
        publicWrite: Boolean,
        sequenced: Boolean,
        retention: Retention,
    ): Status<CreateResponse> = withContext(context) {
        Status.fromResponse(
            service.createChannel(
                accountId,
                CreateRequest(publicRead, publicWrite, sequenced, retention)
            )
        )
    }
}
