package com.nchain.spvchannels.channels.models.api

import com.nchain.spvchannels.channels.models.Channel
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class ChannelsResponse(
    @Json(name = "channels") val channels: List<Channel>
)
