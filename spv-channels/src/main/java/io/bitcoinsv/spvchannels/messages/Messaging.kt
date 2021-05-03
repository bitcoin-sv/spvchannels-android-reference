// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.messages

import io.bitcoinsv.spvchannels.encryption.Encryption
import io.bitcoinsv.spvchannels.encryption.RawMessage
import io.bitcoinsv.spvchannels.firebase.DefaultSpvMessagingService
import io.bitcoinsv.spvchannels.messages.models.ContentType
import io.bitcoinsv.spvchannels.messages.models.ReadRequest
import io.bitcoinsv.spvchannels.notifications.Notification
import io.bitcoinsv.spvchannels.notifications.NotificationService
import io.bitcoinsv.spvchannels.notifications.models.TokenRequest
import io.bitcoinsv.spvchannels.response.ChannelsError
import io.bitcoinsv.spvchannels.response.Status
import io.bitcoinsv.spvchannels.util.StatusMapping
import kotlin.coroutines.CoroutineContext
import kotlinx.coroutines.withContext
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Converter

/**
 * The [Messaging] APIs allow account holders, third parties, or even general public to read from,
 * or write to [io.bitcoinsv.spvchannels.channels.Channel].
 *
 */
class Messaging internal constructor(
    private val messageService: MessageService,
    private val notificationService: NotificationService,
    override val errorConverter: Converter<ResponseBody, ChannelsError>,
    private val channelId: String,
    private val apiToken: String,
    private val fetchToken: suspend () -> String,
    private val encryption: Encryption,
    private val context: CoroutineContext,
) : StatusMapping {
    /**
     * Returns the current max sequence in channel.
     */
    suspend fun getMaxSequence(): Status<String> = withContext(context) {
        val response = messageService.getMaxSequence(channelId)
        if (response.isSuccessful) {
            val header = response.headers()["ETag"] ?: ""
            Status.Success(header)
        } else Status.fromResponse(errorConverter, response) { "ignored" }
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
        mapFromResponse({ RawMessage(it, encryption) }) {
            messageService.sendMessage(
                channelId, encryption.encrypt(message).toRequestBody(contentType.mediaType)
            )
        }
    }

    /**
     * Gets all messages from the Channel, optionally filtered to just unread.
     *
     * @param unreadOnly whether or not to return only unread messages. Defaults to true.
     */
    suspend fun getAllMessages(
        unreadOnly: Boolean = true
    ): Status<List<RawMessage>> = withContext(context) {
        mapFromResponse({
            it.map { message -> RawMessage(message, encryption) }
        }) {
            messageService.getMessages(channelId, unreadOnly)
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
        fromResponse {
            messageService.markMessageRead(channelId, sequenceId, markOlder, ReadRequest(read))
        }
    }

    /**
     * Delete a message from a Channel.
     *
     * @param sequenceId the ID of the message to delete.
     */
    suspend fun deleteMessage(
        sequenceId: String,
    ): Status<Unit> = withContext(context) {
        fromResponse {
            messageService.deleteMessage(channelId, sequenceId)
        }
    }

    /**
     * Register for push notifications.
     *
     * @param listener the listener that will receive notifications when the app is in foreground
     */
    suspend fun registerForNotifications(
        listener: (Notification) -> Unit
    ) = withContext(context) {
        DefaultSpvMessagingService.registerForNotifications(channelId, listener)
        val token = fetchToken()

        fromResponse {
            notificationService.registerFcmToken("Bearer $apiToken", TokenRequest(token))
        }
    }

    /**
     * Deregister push notification for current channel.
     */
    suspend fun deregisterNotifications() = withContext(context) {
        val token = fetchToken()

        fromResponse {
            notificationService.deleteToken(token, channelId)
        }
    }
}
