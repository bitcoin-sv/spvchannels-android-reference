
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.channels.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class AccessToken(
    @Json(name = "id") val id: String?,
    @Json(name = "token") val token: String?,
    @Json(name = "description") val description: String?,
    @Json(name = "can_read") val readable: Boolean,
    @Json(name = "can_write") val writable: Boolean,
)
