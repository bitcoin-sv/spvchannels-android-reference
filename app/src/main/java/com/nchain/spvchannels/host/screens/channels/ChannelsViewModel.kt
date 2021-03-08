package com.nchain.spvchannels.host.screens.channels

import androidx.lifecycle.SavedStateHandle
import com.nchain.spvchannels.SpvChannelsSdk
import com.nchain.spvchannels.host.screens.binding.CommonViewModel

class ChannelsViewModel(savedStateHandle: SavedStateHandle) : CommonViewModel(savedStateHandle) {
    private val args by navArgs<ChannelsFragmentArgs>()
    private val scvChannels by lazy {
        SpvChannelsSdk(args.baseUrl).channelWithCredentials(args.username, args.password)
    }
}
