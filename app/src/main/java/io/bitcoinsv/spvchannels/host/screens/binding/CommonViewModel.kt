
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.binding

import android.os.Bundle
import androidx.annotation.MainThread
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavArgs
import androidx.navigation.NavArgsLazy
import io.bitcoinsv.spvchannels.host.navigation.NavigationAction
import io.bitcoinsv.spvchannels.host.util.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class CommonViewModel(protected val savedStateHandle: SavedStateHandle) : ViewModel() {
    protected val navFlow = MutableSharedFlow<NavigationAction>()
    val navDirections = navFlow.map { Event(it) }.asLiveData()
    val arguments get() = savedStateHandle.get<Bundle>(BindingFragment.BUNDLE_ARGS)

    @MainThread
    inline fun <reified Args : NavArgs> navArgs() = NavArgsLazy(Args::class) {
        arguments ?: throw IllegalStateException("ViewModel $this has no arguments")
    }

    fun <T> MutableSharedFlow<T>.emitInScope(value: T) = viewModelScope.launch {
        emit(value)
    }
}
