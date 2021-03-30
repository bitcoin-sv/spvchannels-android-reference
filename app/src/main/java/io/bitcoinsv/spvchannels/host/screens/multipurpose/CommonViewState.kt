package io.bitcoinsv.spvchannels.host.screens.multipurpose

import androidx.databinding.BaseObservable

abstract class CommonViewState : BaseObservable() {
    var response = ""
        set(value) {
            field = value
            notifyChange()
        }
}
