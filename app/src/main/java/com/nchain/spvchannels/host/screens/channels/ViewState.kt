package com.nchain.spvchannels.host.screens.channels

import androidx.databinding.BaseObservable

class ViewState : BaseObservable() {
    var read = false
    var write = false
    var sequenced = false
    var minAge = "0"
    var maxAge = "0"
    var autoPrune = false
    var response = ""
        set(value) {
            field = value
            notifyChange()
        }
}
