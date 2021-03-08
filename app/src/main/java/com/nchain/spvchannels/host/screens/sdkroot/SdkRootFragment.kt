package com.nchain.spvchannels.host.screens.sdkroot

import androidx.fragment.app.viewModels
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.databinding.FragmentSdkrootBinding
import com.nchain.spvchannels.host.screens.binding.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SdkRootFragment : BindingFragment<FragmentSdkrootBinding, SdkRootViewModel>() {
    override val layout = R.layout.fragment_sdkroot
    override val viewModel by viewModels<SdkRootViewModel>()
}
