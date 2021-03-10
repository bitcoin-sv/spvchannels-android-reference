package com.nchain.spvchannels.channels.models.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class CreateTokenRequest(
    @Json(name = "description") val description: String,
    @Json(name = "can_read") val canRead: Boolean,
    @Json(name = "can_write") val canWrite: Boolean,
)
