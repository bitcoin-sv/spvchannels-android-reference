// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.screens.messages

import io.bitcoinsv.spvchannels.host.screens.multipurpose.CommonViewState

class ViewState : CommonViewState() {
    var contentType = "text/plain"
    var message = ""
    var unreadOnly = false
    var messageRead = false
    var older = false
    var messageId = ""
}
