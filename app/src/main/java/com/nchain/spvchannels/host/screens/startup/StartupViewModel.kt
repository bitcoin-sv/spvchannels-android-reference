package com.nchain.spvchannels.host.screens.startup

import com.nchain.spvchannels.host.navigation.NavigationAction
import com.nchain.spvchannels.host.screens.binding.CommonViewModel

class StartupViewModel : CommonViewModel() {
    var url = "https://localhost:5010"

    fun apply() {
        navFlow.emitInScope(
            NavigationAction.NavigateTo(
                StartupFragmentDirections.actionStartupFragmentToSdkRootFragment(
                    url
                )
            )
        )
    }
}
