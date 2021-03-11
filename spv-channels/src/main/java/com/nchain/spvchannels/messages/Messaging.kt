package com.nchain.spvchannels.messages

import com.nchain.spvchannels.encryption.Encryption
import com.nchain.spvchannels.encryption.RawMessage
import com.nchain.spvchannels.messages.models.ContentType
import com.nchain.spvchannels.messages.models.ReadRequest
import com.nchain.spvchannels.response.Status
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext

class Messaging internal constructor(
    private val messageService: MessageService,
    private val channelId: String,
    private val encryption: Encryption,
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
    ): Status<RawMessage> = withContext(context) {
        Status.fromResponse(
            messageService.sendMessage(
                channelId, contentType.mediaType, encryption.encrypt(message)
            )
        ) { RawMessage(it, encryption) }
    }

    suspend fun getAllMessages(
        unreadOnly: Boolean = true
    ): Status<List<RawMessage>> = withContext(context) {
        Status.fromResponse(
            messageService.getMessages(channelId, unreadOnly)
        ) {
            it.map { message -> RawMessage(message, encryption) }
        }
    }

    suspend fun markMessageRead(
        sequenceId: String,
        read: Boolean,
        markOlder: Boolean? = null
    ): Status<List<Message>> = withContext(context) {
        Status.fromResponse(
            messageService.markMessageRead(channelId, sequenceId, markOlder, ReadRequest(read))
        )
    }

    suspend fun deleteMessage(
        sequenceId: String,
    ): Status<Unit> = withContext(context) {
        Status.fromResponse(
            messageService.deleteMessage(channelId, sequenceId)
        )
    }
}
