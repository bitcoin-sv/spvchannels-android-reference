
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.encryption

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
