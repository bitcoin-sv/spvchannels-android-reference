// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.channels.models.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.bitcoinsv.spvchannels.channels.models.ChannelInfo

@JsonClass(generateAdapter = true)
internal class ChannelsResponse(
    @Json(name = "channels") val channels: List<ChannelInfo>
)
