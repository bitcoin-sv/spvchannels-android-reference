package com.nchain.spvchannels.host.screens.binding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.nchain.spvchannels.host.navigation.NavigationAction
import com.nchain.spvchannels.host.util.Event
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

abstract class CommonViewModel : ViewModel() {
    protected val navFlow = MutableSharedFlow<NavigationAction>()
    val navDirections = navFlow.map { Event(it) }.asLiveData()

    fun <T> MutableSharedFlow<T>.emitInScope(value: T) = viewModelScope.launch {
        emit(value)
    }
}
