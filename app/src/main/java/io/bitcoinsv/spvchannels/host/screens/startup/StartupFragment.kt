
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.startup

import androidx.fragment.app.viewModels
import dagger.hilt.android.AndroidEntryPoint
import io.bitcoinsv.spvchannels.host.R
import io.bitcoinsv.spvchannels.host.databinding.FragmentStartupBinding
import io.bitcoinsv.spvchannels.host.screens.binding.BindingFragment

@AndroidEntryPoint
class StartupFragment : BindingFragment<FragmentStartupBinding, StartupViewModel>() {
    override val layout = R.layout.fragment_startup
    override val viewModel by viewModels<StartupViewModel>()
}
