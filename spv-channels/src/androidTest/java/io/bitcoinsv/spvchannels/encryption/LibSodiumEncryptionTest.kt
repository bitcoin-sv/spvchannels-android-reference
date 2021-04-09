
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.encryption

import io.bitcoinsv.spvchannels.encryption.sodium.LibSodiumEncryption
import java.nio.charset.StandardCharsets
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class LibSodiumEncryptionTest {
    @Test
    fun importBytesAsKeysYieldsCorrectEncryption() {
        val encryption = LibSodiumEncryption.Builder()
            .withKeyPair(
                byteArrayOf(
                    -78, 95, -115, -112, -98, 42, 55, -51, -63, -29, 30, 49, -4, 0, 50, 46, -97,
                    111, 57, -65, 36, -14, 9, 73, 73, -42, 29, -47, 105, -95, -119, 27
                ),
                byteArrayOf(
                    -61, -109, 57, 86, -1, 57, 125, 11, 78, -64, 60, -75, -11, -65, -48, 14, 32,
                    -75, -30, -110, 26, -43, 86, -82, -15, -114, 13, -62, 19, 90, 84, -121
                )
            )
            .build()

        val encrypted = encryption.encrypt(MESSAGE.toByteArray())
        val decrypted = encryption.decrypt(encrypted)

        assertEquals(MESSAGE, decrypted.toString(StandardCharsets.UTF_8))
    }

    @Test
    fun importJsonYieldsCorrectEncryption() {
        val encryption = LibSodiumEncryption.Builder()
            .withSerializedKeyPair(SAMPLE_IMPORT)
            .build()

        val encrypted = encryption.encrypt(MESSAGE.toByteArray())
        val decrypted = encryption.decrypt(encrypted)

        assertEquals(MESSAGE, decrypted.toString(StandardCharsets.UTF_8))
    }

    @Test
    fun getEncryptionKeyExportsFormatted() {
        val encryption = LibSodiumEncryption.Builder()
            .generate()
            .build()
        val encryptionKey = encryption.getEncryptionKey()

        assertTrue(encryptionKey.matches(KEY_REGEX))
    }

    @Test
    fun exportKeysExportsCorrectJson() {
        val encryption = LibSodiumEncryption.Builder()
            .generate()
            .build()
        val exported = encryption.exportKeys()

        val jsonObject = JSONObject(exported)
        assertTrue(jsonObject.has(PUBLIC_KEY))
        assertTrue(jsonObject.has(SECRET_KEY))
        assertTrue(jsonObject.get(PUBLIC_KEY) is JSONArray)
        assertTrue(jsonObject.get(SECRET_KEY) is JSONArray)
    }

    companion object {
        const val PUBLIC_KEY = "publicKey"
        const val SECRET_KEY = "privateKey"
        const val MESSAGE = "abcdef"
        const val KEY_PATTERN =
            "libsodium sealed_box (?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?\$"
        val KEY_REGEX = KEY_PATTERN.toRegex()

        // Sample keys to test importing **DO NOT** use them anywhere else.
        const val SAMPLE_IMPORT =
            "{\"publicKey\":[-78, 95, -115, -112, -98, 42, 55, -51, -63, -29, 30, 49, -4, 0, 50, 46, -97, 111, 57, -65, 36, -14, 9, 73, 73, -42, 29, -47, 105, -95, -119, 27],\"privateKey\":[-61, -109, 57, 86, -1, 57, 125, 11, 78, -64, 60, -75, -11, -65, -48, 14, 32, -75, -30, -110, 26, -43, 86, -82, -15, -114, 13, -62, 19, 90, 84, -121]}" // ktlint-disable max-line-length
    }
}
