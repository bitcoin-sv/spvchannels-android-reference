package com.nchain.spvchannels.encryption.sodium

import android.util.Base64
import com.goterl.lazycode.lazysodium.LazySodiumAndroid
import com.goterl.lazycode.lazysodium.SodiumAndroid
import com.goterl.lazycode.lazysodium.interfaces.Box
import com.goterl.lazycode.lazysodium.utils.Key
import com.goterl.lazycode.lazysodium.utils.KeyPair
import com.nchain.spvchannels.encryption.Encryption
import com.squareup.moshi.Moshi
import java.util.Arrays

/**
 * Encryption using LibSodium secret_box implementation.
 */
class LibSodiumEncryption private constructor(
    private val keyPair: KeyPair,
    private val encryptionKey: Key,
    private val box: LazySodiumAndroid,
) : Encryption {

    override fun encrypt(input: ByteArray): ByteArray {
        val cipher = ByteArray(Box.SEALBYTES + input.size)
        box.cryptoBoxSeal(cipher, input, input.size.toLong(), encryptionKey.asBytes)
        return cipher
    }

    override fun decrypt(input: ByteArray): ByteArray {
        val message = ByteArray(input.size - Box.SEALBYTES)
        box.cryptoBoxSealOpen(
            message,
            input,
            input.size.toLong(),
            keyPair.publicKey.asBytes,
            keyPair.secretKey.asBytes
        )
        return message
    }

    /**
     * Export the encryption key that is used by this encryption. Can be shared with others so that
     * they can use it to encrypt data that only you can decrypt.
     *
     * @return a key that can be used by others to encrypt payloads
     */
    fun getEncryptionKey(): String {
        return "libsodium sealed_box ${
        Base64.encodeToString(
            keyPair.publicKey.asBytes,
            Base64.NO_WRAP
        )
        }"
    }

    /**
     * Exports the public/secret key combination. Private key should **never** be shared.
     *
     * @return json containing publicKey and privateKey fields.
     */
    fun exportKeys(): String {
        return """{"publicKey":${
        Arrays.toString(keyPair.publicKey.asBytes)
        },"privateKey":${
        Arrays.toString(
            keyPair.secretKey.asBytes
        )
        }}"""
    }

    /**
     * [Builder] allows you to create a fully configured instance of [LibSodiumEncryption]
     */
    class Builder {
        private var keyPair: KeyPair? = null
        private var encryptionKey: Key? = null
        private var box: LazySodiumAndroid? = null
        private var generateKeys = false

        /**
         * Specifies the raw bytes to use as public key and secret key. Mutually exclusive with [generate]
         *
         * @param publicKey the public key to use
         * @param secretKey the secret key to use
         */
        fun withKeyPair(publicKey: ByteArray, secretKey: ByteArray): Builder {
            keyPair = KeyPair(
                Key.fromBytes(publicKey),
                Key.fromBytes(secretKey)
            )
            return this
        }

        /**
         * Specifies the JSON from which to load the keypair. Mutually exclusive with [generate]
         *
         * @param json JSON containing publicKey and privateKey fields in
         */
        fun withSerializedKeyPair(json: String): Builder {
            val moshi = Moshi.Builder()
                .build()
            val adapter = moshi.adapter(SerializedKeys::class.java)
            val keyPair = adapter.fromJson(json)
                ?: throw IllegalArgumentException("Unable to deserialize keypair.")
            this.keyPair = KeyPair(
                Key.fromBytes(keyPair.publicKey),
                Key.fromBytes(keyPair.privateKey),
            )
            return this
        }

        /**
         * Generate new keys when building [LibSodiumEncryption]. Mutually exclusive with [withKeyPair].
         */
        fun generate(): Builder {
            generateKeys = true
            return this
        }

        /**
         * Creates an instance of encryption.
         *
         * @throws IllegalStateException if both keys and generate were set or if keys are missing.
         */
        fun build(): LibSodiumEncryption {
            val box = box ?: LazySodiumAndroid(SodiumAndroid())

            if (generateKeys) {
                if (keyPair != null) {
                    throw IllegalStateException("Requested to generate keys, but they were provided. Either remove generate() or do not set the keys.") // ktlint-disable max-line-length
                }
                keyPair = box.cryptoBoxKeypair()
            }
            if (keyPair == null) {
                throw IllegalStateException("Public/Private keys need to be provided, or generated using generate()") // ktlint-disable max-line-length
            }
            val encryptionKey = encryptionKey ?: keyPair!!.publicKey

            return LibSodiumEncryption(keyPair!!, encryptionKey, box)
        }
    }
}
