// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.notifications

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.Date

data class Notification(
    val time: Date,
    val message: String,
) {
    constructor(time: String, message: String) : this(DATE_FORMAT.parse(time)!!, message)

    // "2021-04-20T10:27:46.0792886Z"
    companion object {
        // Value will not be displayed, we're formatting data from the server
        @SuppressLint("SimpleDateFormat")
        private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
    }
}
