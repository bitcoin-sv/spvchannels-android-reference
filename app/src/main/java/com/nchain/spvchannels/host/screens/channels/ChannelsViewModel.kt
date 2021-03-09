package com.nchain.spvchannels.host.screens.channels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.nchain.spvchannels.SpvChannelsSdk
import com.nchain.spvchannels.channels.models.Retention
import com.nchain.spvchannels.host.logging.ObjectSerializer
import com.nchain.spvchannels.host.screens.binding.CommonViewModel
import com.nchain.spvchannels.response.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber

@HiltViewModel
class ChannelsViewModel @Inject constructor(
    private val objectSerializer: ObjectSerializer,
    savedStateHandle: SavedStateHandle
) :
    CommonViewModel(savedStateHandle) {
    private val args by navArgs<ChannelsFragmentArgs>()
    private val channels by lazy {
        SpvChannelsSdk(args.baseUrl).channelWithCredentials(
            args.accountId,
            args.username,
            args.password
        )
    }
    val state = ViewState()

    fun createChannel() = launchCatching {
        val response = channels.createChannel(
            state.read, state.write, state.sequenced,
            Retention(
                state.minAge.toIntOrNull(),
                state.maxAge.toIntOrNull(),
                state.autoPrune,
            )
        )

        state.response = when (response) {
            is Status.Success -> objectSerializer.serialize(response.value) ?: ""
            Status.Forbidden -> "Forbidden"
            Status.NotFound -> "Not found"
            Status.ServerError -> "Server error"
            Status.Unauthorized -> "Unauthorized"
            else -> response.toString()
        }
    }

    private fun launchCatching(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
        try {
            block()
        } catch (e: Exception) {
            Timber.e(e)
            state.response = e.toString()
        }
    }
}
