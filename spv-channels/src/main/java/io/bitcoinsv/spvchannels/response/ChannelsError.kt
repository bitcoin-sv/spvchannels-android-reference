// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.response

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ChannelsError(
    @Json(name = "type") val type: String,
    @Json(name = "title") val title: String,
    @Json(name = "status") val status: Int,
    @Json(name = "traceId") val traceId: String,
)
