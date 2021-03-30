package io.bitcoinsv.spvchannels.host.screens.startup

import androidx.lifecycle.SavedStateHandle
import dagger.hilt.android.lifecycle.HiltViewModel
import io.bitcoinsv.spvchannels.host.navigation.NavigationAction
import io.bitcoinsv.spvchannels.host.screens.binding.CommonViewModel
import io.bitcoinsv.spvchannels.host.storage.Storage
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
    var channelId: String
        get() = storage.channelId
        set(value) {
            storage.channelId = value
        }
    var token: String
        get() = storage.token
        set(value) {
            storage.token = value
        }

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
        navFlow.emitInScope(
            NavigationAction.NavigateTo(
                StartupFragmentDirections.actionStartupFragmentToMessagesFragment(
                    url,
                    channelId,
                    token,
                )
            )
        )
    }
}
