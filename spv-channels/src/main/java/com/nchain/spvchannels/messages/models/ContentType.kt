package com.nchain.spvchannels.messages.models

import okhttp3.MediaType.Companion.toMediaType

class ContentType(contentType: String) {
    internal val mediaType = contentType.toMediaType()
}
