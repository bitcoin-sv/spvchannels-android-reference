package com.nchain.spvchannels.host.screens.channels

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nchain.spvchannels.SpvChannelsSdk
import com.nchain.spvchannels.channels.models.Retention
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.logging.ObjectSerializer
import com.nchain.spvchannels.host.options.Option
import com.nchain.spvchannels.host.screens.binding.CommonViewModel
import com.nchain.spvchannels.response.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
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
    val options = listOf(
        Option(
            R.string.msg_list_channels,
            listOf(),
            this::listChannels
        ),
        Option(
            R.string.msg_create_channel,
            listOf(
                R.id.cb_read,
                R.id.cb_write,
                R.id.cb_sequenced,
                R.id.tv_min_age,
                R.id.et_min_age,
                R.id.et_max_age,
                R.id.cb_auto_prune
            ),
            this::createChannel
        ),
        Option(
            R.string.msg_get_channel,
            listOf(
                R.id.et_channel_id
            ),
            this::getChannel
        )
    )
    private val stateFlow = MutableStateFlow(options[0])
    val visibility = stateFlow.asLiveData()
    val state = ViewState()

    private fun listChannels() = launchCatching {
        val response = channels.getAllChannels()

        setResponseText(response)
    }

    private fun createChannel() = launchCatching {
        val response = channels.createChannel(
            state.read, state.write, state.sequenced,
            Retention(
                state.minAge.toIntOrNull(),
                state.maxAge.toIntOrNull(),
                state.autoPrune,
            )
        )

        setResponseText(response)
    }

    private fun getChannel() = launchCatching {
        val response = channels.getChannel(state.channelId)

        setResponseText(response)
    }

    private inline fun <reified T> setResponseText(response: Status<T>) {
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
            state.response = ""
            block()
        } catch (e: Exception) {
            Timber.e(e)
            state.response = e.toString()
        }
    }

    fun selectItem(position: Int) {
        stateFlow.emitInScope(options[position])
    }
}
