// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.channels.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ChannelInfo(
    @Json(name = "id") val id: String?,
    @Json(name = "href") val href: String?,
    @Json(name = "public_read") val publicRead: Boolean,
    @Json(name = "public_write") val publicWrite: Boolean,
    @Json(name = "sequenced") val sequenced: Boolean,
    @Json(name = "locked") val locked: Boolean,
    @Json(name = "head") val head: Long,
    @Json(name = "retention") val retention: Retention,
    @Json(name = "access_tokens") val accessTokens: List<AccessToken>?,
)
