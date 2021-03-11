package com.nchain.spvchannels.encryption

import android.util.Base64
import com.nchain.spvchannels.messages.models.Message
import java.util.Calendar

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
