package com.nchain.spvchannels.messages

import com.nchain.spvchannels.messages.models.ContentType
import com.nchain.spvchannels.messages.models.Message
import com.nchain.spvchannels.response.Status
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

class Messaging internal constructor(
    private val messageService: MessageService,
    private val channelId: String,
    private val context: CoroutineContext,
) {
    suspend fun getMaxSequence(): Status<String> = withContext(context) {
        val response = messageService.getMaxSequence(channelId)
        if (response.isSuccessful) {
            val header = response.headers()["ETag"] ?: ""
            Status.Success(header)
        } else Status.fromResponse(response) { "ignored" }
    }

    suspend fun sendMessage(
        contentType: ContentType,
        message: ByteArray
    ): Status<Message> = withContext(context) {
        Status.fromResponse(
            messageService.sendMessage(
                channelId, contentType.mediaType, message
            )
        )
    }

    suspend fun getAllMessages(
        unreadOnly: Boolean? = null
    ): Status<List<Message>> = withContext(context) {
        Status.fromResponse(
            messageService.getMessages(channelId, unreadOnly)
        )
    }
}
