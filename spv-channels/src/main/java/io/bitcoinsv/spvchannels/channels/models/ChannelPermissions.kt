
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.channels.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ChannelPermissions(
    @Json(name = "public_read") val publicRead: Boolean,
    @Json(name = "public_write") val publicWrite: Boolean,
    @Json(name = "locked") val locked: Boolean,
)
