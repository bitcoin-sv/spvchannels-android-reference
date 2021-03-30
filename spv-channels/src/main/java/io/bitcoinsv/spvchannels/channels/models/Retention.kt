package io.bitcoinsv.spvchannels.channels.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Retention(
    @Json(name = "min_age_days") val minAgeDays: Int?,
    @Json(name = "max_age_days") val maxAgeDays: Int?,
    @Json(name = "auto_prune") val autoPrune: Boolean
)
