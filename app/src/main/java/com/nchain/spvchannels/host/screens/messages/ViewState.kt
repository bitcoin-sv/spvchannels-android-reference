package com.nchain.spvchannels.host.screens.messages

import com.nchain.spvchannels.host.screens.multipurpose.CommonViewState

class ViewState : CommonViewState() {
    var contentType = "text/plain"
    var message = ""
    var unreadOnly = false
    var messageRead = false
    var older = false
    var messageId = ""
}
