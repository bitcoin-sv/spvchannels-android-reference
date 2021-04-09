
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.encryption

import android.util.Base64
import io.bitcoinsv.spvchannels.messages.models.Message
import java.util.Calendar

/**
 * Represents the raw (decrypted) message.
 */
class RawMessage(
    val sequence: Int,
    val date: Calendar,
    val contentType: String?,
    val payload: ByteArray?
) {
    internal constructor(message: Message, encryption: Encryption) : this(
        message.sequence,
        message.date,
        message.contentType,
        message.payload?.let { payload ->
            encryption.decrypt(Base64.decode(payload, Base64.NO_WRAP))
        }
    )
}
