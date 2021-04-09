
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.encryption

/**
 * Used to encrypt and decrypt the payloads of messages.
 */
interface Encryption {
    /**
     * Takes a byte array encrypts it, and returns a byte array.
     *
     * @param input the data to be encrypted
     */
    fun encrypt(input: ByteArray): ByteArray

    /**
     * Decrypts the input and returns the resulting byte array.
     *
     * @param input the data to be decrypted
     */
    fun decrypt(input: ByteArray): ByteArray
}
