package com.nchain.spvchannels.host.screens.startup

import androidx.lifecycle.SavedStateHandle
import com.nchain.spvchannels.host.navigation.NavigationAction
import com.nchain.spvchannels.host.screens.binding.CommonViewModel

class StartupViewModel(savedStateHandle: SavedStateHandle) : CommonViewModel(savedStateHandle) {
    var url = ""
    var username = ""
    var password = ""
    var token = ""

    fun openChannels() {
        navFlow.emitInScope(
            NavigationAction.NavigateTo(
                StartupFragmentDirections.actionStartupFragmentToChannelsFragment(
                    url,
                    username,
                    password
                )
            )
        )
    }

    fun openMessages() {
    }
}
