package com.nchain.spvchannels.host.screens.messages

import androidx.fragment.app.viewModels
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.databinding.FragmentMessagesBinding
import com.nchain.spvchannels.host.screens.multipurpose.MultiPurposeScreenFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessagesFragment : MultiPurposeScreenFragment<FragmentMessagesBinding, MessagesViewModel>() {
    override val layout = R.layout.fragment_messages
    override val viewModel by viewModels<MessagesViewModel>()
    override val layoutWrapper by lazy { binding.layoutWrapper }
    override val button by lazy { binding.btnPerform }
    override val spinner by lazy { binding.spinner }
}
