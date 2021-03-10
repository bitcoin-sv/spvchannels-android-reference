package com.nchain.spvchannels.host.screens.multipurpose

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nchain.spvchannels.host.R
import com.nchain.spvchannels.host.logging.ObjectSerializer
import com.nchain.spvchannels.host.options.Option
import com.nchain.spvchannels.host.screens.binding.CommonViewModel
import com.nchain.spvchannels.response.Status
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
