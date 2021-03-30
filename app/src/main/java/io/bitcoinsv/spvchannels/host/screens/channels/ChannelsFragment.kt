package io.bitcoinsv.spvchannels.host.screens.channels

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.bitcoinsv.spvchannels.host.R
import io.bitcoinsv.spvchannels.host.databinding.FragmentChannelsBinding
import io.bitcoinsv.spvchannels.host.screens.multipurpose.MultiPurposeScreenFragment

@AndroidEntryPoint
class ChannelsFragment : MultiPurposeScreenFragment<FragmentChannelsBinding, ChannelsViewModel>() {
    override val layout = R.layout.fragment_channels
    override val viewModel by viewModels<ChannelsViewModel>()
    override val layoutWrapper by lazy { binding.layoutWrapper }
    override val button by lazy { binding.btnPerform }
    override val spinner by lazy { binding.spinner }
}
