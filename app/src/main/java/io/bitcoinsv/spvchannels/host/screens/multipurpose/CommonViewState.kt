// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.multipurpose

import androidx.databinding.BaseObservable

abstract class CommonViewState : BaseObservable() {
    var response = ""
        set(value) {
            field = value
            notifyChange()
        }
}
