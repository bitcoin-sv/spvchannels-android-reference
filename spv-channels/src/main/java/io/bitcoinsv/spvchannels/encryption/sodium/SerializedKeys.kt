
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.encryption.sodium

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

/**
 * Represents the serialized keys, containing a public and a private key. Revealing the private key
 * may pose a security issue.
 */
@JsonClass(generateAdapter = true)
internal class SerializedKeys(
    @Json(name = "publicKey") val publicKey: ByteArray,
    @Json(name = "privateKey") val privateKey: ByteArray,
)
