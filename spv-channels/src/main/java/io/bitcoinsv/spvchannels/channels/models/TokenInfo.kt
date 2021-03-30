package io.bitcoinsv.spvchannels.channels.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
class TokenInfo(
    @Json(name = "id") val id: String?,
    @Json(name = "token") val token: String?,
    @Json(name = "description") val description: String,
    @Json(name = "can_read") val canRead: Boolean,
    @Json(name = "can_write") val canWrite: Boolean,
)
