// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.channels

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bitcoinsv.spvchannels.channels.models.Retention
import io.bitcoinsv.spvchannels.host.ChannelsSdkHolder
import io.bitcoinsv.spvchannels.host.R
import io.bitcoinsv.spvchannels.host.logging.ObjectSerializer
import io.bitcoinsv.spvchannels.host.options.Option
import io.bitcoinsv.spvchannels.host.screens.multipurpose.MultiPurposeScreenViewModel
import javax.inject.Inject

@HiltViewModel
class ChannelsViewModel @Inject constructor(
    objectSerializer: ObjectSerializer,
    savedStateHandle: SavedStateHandle,
    channelsHolder: ChannelsSdkHolder,
) : MultiPurposeScreenViewModel<ViewState>(objectSerializer, savedStateHandle) {
    private val args by navArgs<ChannelsFragmentArgs>()
    private val channels = channelsHolder.sdkForUrl(args.baseUrl).channelWithCredentials(
        args.accountId,
        args.username,
        args.password
    )
    override val options = listOf(
        Option(
            R.string.msg_list_channels,
            listOf(),
            this::listChannels
        ),
        Option(
            R.string.msg_create_channel,
            listOf(
                R.id.cb_read,
                R.id.cb_write,
                R.id.cb_sequenced,
                R.id.tv_min_age,
                R.id.et_min_age,
                R.id.et_max_age,
                R.id.cb_auto_prune,
            ),
            this::createChannel
        ),
        Option(
            R.string.msg_amend_channel,
            listOf(
                R.id.cb_read,
                R.id.cb_write,
                R.id.cb_locked,
                R.id.tv_channel_id,
                R.id.et_channel_id,
            ),
            this::amendChannel
        ),
        Option(
            R.string.msg_get_channel,
            listOf(
                R.id.et_channel_id,
            ),
            this::getChannel
        ),
        Option(
            R.string.msg_delete_channel,
            listOf(
                R.id.tv_channel_id,
                R.id.et_channel_id,
            ),
            this::deleteChannel
        ),
        Option(
            R.string.msg_get_channel_tokens,
            listOf(
                R.id.tv_channel_id,
                R.id.et_channel_id,
                R.id.tv_token,
                R.id.et_token,
            ),
            this::getChannelTokens
        ),
        Option(
            R.string.msg_get_token_info,
            listOf(
                R.id.tv_channel_id,
                R.id.et_channel_id,
                R.id.tv_token,
                R.id.et_token,
            ),
            this::getTokenInfo
        ),
        Option(
            R.string.msg_create_token,
            listOf(
                R.id.tv_channel_id,
                R.id.et_channel_id,
                R.id.tv_description,
                R.id.et_description,
                R.id.cb_read,
                R.id.cb_write,
            ),
            this::createToken
        ),
        Option(
            R.string.msg_revoke_token,
            listOf(
                R.id.tv_channel_id,
                R.id.et_channel_id,
                R.id.tv_token,
                R.id.et_token,
            ),
            this::revokeToken
        ),
    )
    override val state = ViewState()

    private fun listChannels() = launchCatching {
        val response = channels.getAllChannels()

        setResponseText(response)
    }

    private fun createChannel() = launchCatching {
        val response = channels.createChannel(
            state.read, state.write, state.sequenced,
            Retention(
                state.minAge.toIntOrNull(),
                state.maxAge.toIntOrNull(),
                state.autoPrune,
            )
        )

        setResponseText(response)
    }

    private fun amendChannel() = launchCatching {
        val response = channels.amendChannel(state.channelId, state.read, state.write, state.locked)

        setResponseText(response)
    }

    private fun getChannel() = launchCatching {
        val response = channels.getChannel(state.channelId)

        setResponseText(response)
    }

    private fun deleteChannel() = launchCatching {
        val response = channels.deleteChannel(state.channelId)

        setResponseText(response)
    }

    private fun getChannelTokens() = launchCatching {
        val token = if (state.token.isEmpty()) null else state.token
        val response = channels.getChannelTokens(state.channelId, token)

        setResponseText(response)
    }

    private fun getTokenInfo() = launchCatching {
        val response = channels.getTokenInfo(state.channelId, state.token)

        setResponseText(response)
    }

    private fun createToken() = launchCatching {
        val response = channels.createToken(
            state.channelId,
            state.description,
            state.read,
            state.write
        )

        setResponseText(response)
    }

    private fun revokeToken() = launchCatching {
        val response = channels.revokeToken(state.channelId, state.token)

        setResponseText(response)
    }
}
