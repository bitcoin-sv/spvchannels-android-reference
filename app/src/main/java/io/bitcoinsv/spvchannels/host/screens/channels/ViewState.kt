package io.bitcoinsv.spvchannels.host.screens.channels

import io.bitcoinsv.spvchannels.host.screens.multipurpose.CommonViewState

class ViewState : CommonViewState() {
    var read = false
    var write = false
    var sequenced = false
    var locked = false
    var minAge = "0"
    var maxAge = "0"
    var autoPrune = false
    var channelId = ""
    var token = ""
    var description = ""
}
