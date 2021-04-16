// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.notifications.models

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Model specifying the token to send.
 *
 * @param token the token to send
 */
@JsonClass(generateAdapter = true)
class TokenRequest(
    @Json(name = "token") val token: String,
)
