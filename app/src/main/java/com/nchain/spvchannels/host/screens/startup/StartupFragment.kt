package com.nchain.spvchannels.host.screens.startup

import androidx.fragment.app.viewModels
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.databinding.FragmentStartupBinding
import com.nchain.spvchannels.host.screens.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartupFragment : BindingFragment<FragmentStartupBinding, StartupViewModel>() {
    override val layout = R.layout.fragment_startup
    override val viewModel by viewModels<StartupViewModel>()
}
