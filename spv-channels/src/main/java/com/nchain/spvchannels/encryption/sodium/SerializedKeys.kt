package com.nchain.spvchannels.encryption.sodium

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
internal class SerializedKeys(
    @Json(name = "publicKey") val publicKey: ByteArray,
    @Json(name = "privateKey") val privateKey: ByteArray,
)
