package com.nchain.spvchannels.channels.models.api

import com.nchain.spvchannels.channels.models.Retention
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class CreateRequest(
    @Json(name = "public_read") val publicRead: Boolean,
    @Json(name = "public_write") val publicWrite: Boolean,
    @Json(name = "sequenced") val sequenced: Boolean,
    @Json(name = "retention") val retention: Retention,
)
