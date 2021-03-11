package com.nchain.spvchannels.encryption

/**
 * Default encryption used, does nothing, just returns inputs.
 */
class NoOpEncryption : Encryption {
    override fun encrypt(input: ByteArray): ByteArray {
        return input
    }

    override fun decrypt(input: ByteArray): ByteArray {
        return input
    }
}
