package io.bitcoinsv.spvchannels.messages.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class ReadRequest(
    @Json(name = "read") val read: Boolean,
)
