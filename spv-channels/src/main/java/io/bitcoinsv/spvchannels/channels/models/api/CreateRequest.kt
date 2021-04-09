
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.channels.models.api

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import io.bitcoinsv.spvchannels.channels.models.Retention

@JsonClass(generateAdapter = true)
internal data class CreateRequest(
    @Json(name = "public_read") val publicRead: Boolean,
    @Json(name = "public_write") val publicWrite: Boolean,
    @Json(name = "sequenced") val sequenced: Boolean,
    @Json(name = "retention") val retention: Retention,
)
