// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.messages

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bitcoinsv.spvchannels.encryption.NoOpEncryption
import io.bitcoinsv.spvchannels.encryption.sodium.LibSodiumEncryption
import io.bitcoinsv.spvchannels.host.ChannelsSdkHolder
import io.bitcoinsv.spvchannels.host.R
import io.bitcoinsv.spvchannels.host.logging.ObjectSerializer
import io.bitcoinsv.spvchannels.host.options.Option
import io.bitcoinsv.spvchannels.host.screens.multipurpose.MultiPurposeScreenViewModel
import io.bitcoinsv.spvchannels.host.storage.Storage
import io.bitcoinsv.spvchannels.host.util.Event
import io.bitcoinsv.spvchannels.messages.models.ContentType
import io.bitcoinsv.spvchannels.notifications.Notification
import io.bitcoinsv.spvchannels.response.Status
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import okio.ByteString.Companion.decodeBase64

@HiltViewModel
class MessagesViewModel @Inject constructor(
    objectSerializer: ObjectSerializer,
    savedStateHandle: SavedStateHandle,
    channelsSdkHolder: ChannelsSdkHolder,
    private val storage: Storage,
) : MultiPurposeScreenViewModel<ViewState>(objectSerializer, savedStateHandle) {
    private val args by navArgs<MessagesFragmentArgs>()
    private val messages = channelsSdkHolder.sdkForUrl(args.baseUrl).let {
        val encrpytion = if (args.encrypt) {
            LibSodiumEncryption.Builder()
                .withKeyPair(PUBLIC_KEY, SECRET_KEY)
                .build()
        } else {
            NoOpEncryption()
        }

        it.messagingWithToken(args.channelId, args.token, encrpytion)
    }
    private val notificationFlow = MutableSharedFlow<Notification>()
    val notification = notificationFlow.map { Event(it) }.asLiveData()

    override val options = listOf(
        Option(
            R.string.msg_get_max_sequence,
            listOf(),
            this::getMaxSequence
        ),
        Option(
            R.string.msg_send_message,
            listOf(
                R.id.tv_type,
                R.id.et_type,
                R.id.tv_message,
                R.id.et_message,
            ),
            this::sendMessage
        ),
        Option(
            R.string.msg_get_all_messages,
            listOf(
                R.id.cb_unread,
            ),
            this::getAllMessages
        ),
        Option(
            R.string.msg_mark_read,
            listOf(
                R.id.tv_message_id,
                R.id.et_message_id,
                R.id.cb_read,
                R.id.cb_older,
            ),
            this::markMessageRead
        ),
        Option(
            R.string.msg_delete_message,
            listOf(
                R.id.tv_message_id,
                R.id.et_message_id,
            ),
            this::deleteMessage
        ),
    )
    override val state = ViewState()

    fun getNotificationsEnabled() = storage.notificationsEnabled

    fun setNotificationsEnabled(enabled: Boolean) = launchCatching {
        val result = if (enabled) {
            messages.registerForNotifications {
                notificationFlow.emitInScope(it)
            }
        } else {
            messages.deregisterNotifications()
        }

        setResponseText(result)
        if (result is Status.Success) {
            storage.notificationsEnabled = enabled
        }
    }

    private fun getMaxSequence() = launchCatching {
        val result = messages.getMaxSequence()

        setResponseText(result)
    }

    private fun sendMessage() = launchCatching {
        val state = messages.sendMessage(
            ContentType(state.contentType),
            state.message.toByteArray()
        )

        setResponseText(state)
    }

    private fun getAllMessages() = launchCatching {
        val result = messages.getAllMessages(state.unreadOnly)

        setResponseText(result)
    }

    private fun markMessageRead() = launchCatching {
        val result = messages.markMessageRead(state.messageId, state.messageRead, state.older)

        setResponseText(result)
    }

    private fun deleteMessage() = launchCatching {
        val result = messages.deleteMessage(state.messageId)

        setResponseText(result)
    }

    companion object {
        val PUBLIC_KEY =
            "3uNk31m6mtuKeK/f6U5EFREilaahQR2PewReP3Cypzg=".decodeBase64()!!.toByteArray()
        val SECRET_KEY =
            "Pr9g/f79rGIlIq+NpsXreUifC5KTTwRYLaWTYjhHfY8=".decodeBase64()!!.toByteArray()
    }
}
