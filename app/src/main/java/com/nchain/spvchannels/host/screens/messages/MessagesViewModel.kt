package com.nchain.spvchannels.host.screens.messages

import androidx.lifecycle.SavedStateHandle
import com.nchain.spvchannels.host.ChannelsSdkHolder
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.logging.ObjectSerializer
import com.nchain.spvchannels.host.options.Option
import com.nchain.spvchannels.host.screens.multipurpose.MultiPurposeScreenViewModel
import com.nchain.spvchannels.messages.models.ContentType
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    objectSerializer: ObjectSerializer,
    savedStateHandle: SavedStateHandle,
    channelsSdkHolder: ChannelsSdkHolder,
) : MultiPurposeScreenViewModel<ViewState>(objectSerializer, savedStateHandle) {
    private val args by navArgs<MessagesFragmentArgs>()
    private val messages = channelsSdkHolder.sdkForUrl(args.baseUrl)
        .messagingWithToken(args.channelId, args.token)

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
}
