package io.bitcoinsv.spvchannels.messages.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import java.util.Calendar

@JsonClass(generateAdapter = true)
internal data class Message(
    @Json(name = "sequence") val sequence: Int,
    @Json(name = "received") val date: Calendar,
    @Json(name = "content_type") val contentType: String?,
    @Json(name = "payload") val payload: String?,
)
