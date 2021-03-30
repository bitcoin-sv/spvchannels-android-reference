package io.bitcoinsv.spvchannels.host.screens.messages

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.bitcoinsv.spvchannels.host.R
import io.bitcoinsv.spvchannels.host.databinding.FragmentMessagesBinding
import io.bitcoinsv.spvchannels.host.screens.multipurpose.MultiPurposeScreenFragment

@AndroidEntryPoint
class MessagesFragment : MultiPurposeScreenFragment<FragmentMessagesBinding, MessagesViewModel>() {
    override val layout = R.layout.fragment_messages
    override val viewModel by viewModels<MessagesViewModel>()
    override val layoutWrapper by lazy { binding.layoutWrapper }
    override val button by lazy { binding.btnPerform }
    override val spinner by lazy { binding.spinner }
}
