package com.nchain.spvchannels.host.screens.channels

import androidx.fragment.app.viewModels
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.databinding.FragmentChannelsBinding
import com.nchain.spvchannels.host.screens.multipurpose.MultiPurposeScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ChannelsFragment : MultiPurposeScreenFragment<FragmentChannelsBinding, ChannelsViewModel>() {
    override val layout = R.layout.fragment_channels
    override val viewModel by viewModels<ChannelsViewModel>()
    override val layoutWrapper by lazy { binding.layoutWrapper }
    override val button by lazy { binding.btnPerform }
    override val spinner by lazy { binding.spinner }
}
