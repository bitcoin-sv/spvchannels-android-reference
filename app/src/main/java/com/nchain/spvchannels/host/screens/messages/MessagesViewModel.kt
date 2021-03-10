package com.nchain.spvchannels.host.screens.messages

import androidx.lifecycle.SavedStateHandle
import com.nchain.spvchannels.SpvChannelsSdk
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.logging.ObjectSerializer
import com.nchain.spvchannels.host.options.Option
import com.nchain.spvchannels.host.screens.multipurpose.MultiPurposeScreenViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(
    objectSerializer: ObjectSerializer,
    savedStateHandle: SavedStateHandle,
) : MultiPurposeScreenViewModel<ViewState>(objectSerializer, savedStateHandle) {
    private val args by navArgs<MessagesFragmentArgs>()
    private val messages by lazy {
        SpvChannelsSdk(args.baseUrl).messagingWithToken(args.channelId, args.token)
    }

    override val options = listOf(
        // TODO: replace with actual option - at least one needs to be present
        Option(
            R.string.app_name,
            listOf(),
            {}
        )
    )
    override val state = ViewState()
}
