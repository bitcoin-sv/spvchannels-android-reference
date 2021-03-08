package com.nchain.spvchannels.host.screens.channels

import androidx.fragment.app.viewModels
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.databinding.FragmentChannelsBinding
import com.nchain.spvchannels.host.screens.binding.BindingFragment

class ChannelsFragment : BindingFragment<FragmentChannelsBinding, ChannelsViewModel>() {
    override val layout = R.layout.fragment_channels
    override val viewModel by viewModels<ChannelsViewModel>()
}
