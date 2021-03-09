package com.nchain.spvchannels.host.screens.startup

import androidx.lifecycle.SavedStateHandle
import com.nchain.spvchannels.host.navigation.NavigationAction
import com.nchain.spvchannels.host.screens.binding.CommonViewModel
import com.nchain.spvchannels.host.storage.Storage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StartupViewModel @Inject constructor(
    private val storage: Storage,
    savedStateHandle: SavedStateHandle
) : CommonViewModel(savedStateHandle) {
    var url: String
        get() = storage.url
        set(value) {
            storage.url = value
        }
    var accountId: String
        get() = storage.account
        set(value) {
            storage.account = value
        }
    var username: String
        get() = storage.username
        set(value) {
            storage.username = value
        }
    var password: String
        get() = storage.password
        set(value) {
            storage.password = value
        }
    var token = ""

    fun openChannels() {
        navFlow.emitInScope(
            NavigationAction.NavigateTo(
                StartupFragmentDirections.actionStartupFragmentToChannelsFragment(
                    url,
                    username,
                    password,
                    accountId,
                )
            )
        )
    }

    fun openMessages() {
    }
}
