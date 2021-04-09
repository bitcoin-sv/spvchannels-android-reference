// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.messages

import io.bitcoinsv.spvchannels.encryption.Encryption
import io.bitcoinsv.spvchannels.encryption.RawMessage
import io.bitcoinsv.spvchannels.messages.models.ContentType
import io.bitcoinsv.spvchannels.messages.models.ReadRequest
import io.bitcoinsv.spvchannels.response.Status
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * The [Messaging] APIs allow account holders, third parties, or even general public to read from,
 * or write to [io.bitcoinsv.spvchannels.channels.Channel].
 *
 */
class Messaging internal constructor(
    private val messageService: MessageService,
    private val channelId: String,
    private val encryption: Encryption,
    private val context: CoroutineContext,
) {
    /**
     * Returns the current max sequence in channel.
     */
    suspend fun getMaxSequence(): Status<String> = withContext(context) {
        val response = messageService.getMaxSequence(channelId)
        if (response.isSuccessful) {
            val header = response.headers()["ETag"] ?: ""
            Status.Success(header)
        } else Status.fromResponse(response) { "ignored" }
    }

    /**
     * Writes a message to a Channel.
     *
     * @param contentType the content type of the message
     * @param message the raw bytes of the message
     */
    suspend fun sendMessage(
        contentType: ContentType,
        message: ByteArray
    ): Status<RawMessage> = withContext(context) {
        Status.fromResponse(
            messageService.sendMessage(
                channelId, encryption.encrypt(message).toRequestBody(contentType.mediaType)
            )
        ) { RawMessage(it, encryption) }
    }

    /**
     * Gets all messages from the Channel, optionally filtered to just unread.
     *
     * @param unreadOnly whether or not to return only unread messages. Defaults to true.
     */
    suspend fun getAllMessages(
        unreadOnly: Boolean = true
    ): Status<List<RawMessage>> = withContext(context) {
        Status.fromResponse(
            messageService.getMessages(channelId, unreadOnly)
        ) {
            it.map { message -> RawMessage(message, encryption) }
        }
    }

    /**
     * Flags a message as read or unread.
     *
     * @param sequenceId the id of the message
     * @param read whether to mark the message as read or unread
     * @param markOlder whether or not to mark older messages as well
     */
    suspend fun markMessageRead(
        sequenceId: String,
        read: Boolean,
        markOlder: Boolean? = null
    ): Status<Unit> = withContext(context) {
        Status.fromResponse(
            messageService.markMessageRead(channelId, sequenceId, markOlder, ReadRequest(read))
        )
    }

    /**
     * Delete a message from a Channel.
     *
     * @param sequenceId the ID of the message to delete.
     */
    suspend fun deleteMessage(
        sequenceId: String,
    ): Status<Unit> = withContext(context) {
        Status.fromResponse(
            messageService.deleteMessage(channelId, sequenceId)
        )
    }
}
