// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.multipurpose

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import io.bitcoinsv.spvchannels.host.R
import io.bitcoinsv.spvchannels.host.logging.ObjectSerializer
import io.bitcoinsv.spvchannels.host.options.Option
import io.bitcoinsv.spvchannels.host.screens.binding.CommonViewModel
import io.bitcoinsv.spvchannels.response.Status
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

abstract class MultiPurposeScreenViewModel<State : CommonViewState>(
    protected val objectSerializer: ObjectSerializer,
    savedStateHandle: SavedStateHandle
) : CommonViewModel(savedStateHandle) {
    abstract val options: List<Option>
    private val stateFlow = MutableStateFlow(
        Option(
            R.string.msg_select_action,
            listOf()
        ) {}
    )
    val visibility = stateFlow.asLiveData()

    abstract val state: State

    protected inline fun <reified T> setResponseText(response: Status<T>) {
        state.response = when (response) {
            is Status.Success -> objectSerializer.serialize(response.value) ?: ""
            Status.Forbidden -> "Forbidden"
            Status.NotFound -> "Not found"
            Status.ServerError -> "Server error"
            Status.Unauthorized -> "Unauthorized"
            Status.NoContent -> "No content"
            else -> response.toString()
        }
    }

    protected fun launchCatching(block: suspend CoroutineScope.() -> Unit) = viewModelScope.launch {
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
