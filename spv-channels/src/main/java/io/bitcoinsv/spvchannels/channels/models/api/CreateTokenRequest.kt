
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.channels.models.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class CreateTokenRequest(
    @Json(name = "description") val description: String,
    @Json(name = "can_read") val canRead: Boolean,
    @Json(name = "can_write") val canWrite: Boolean,
)
