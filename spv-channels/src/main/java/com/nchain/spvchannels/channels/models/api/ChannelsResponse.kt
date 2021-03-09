package com.nchain.spvchannels.channels.models.api

import com.nchain.spvchannels.channels.models.ChannelInfo
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ChannelsResponse(
    @Json(name = "channels") val channels: List<ChannelInfo>
)
