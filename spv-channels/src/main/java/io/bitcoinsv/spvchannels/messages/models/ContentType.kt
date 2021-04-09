// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.messages.models

import okhttp3.MediaType.Companion.toMediaType

/**
 * Specifies a content type for a [Message]
 *
 * @param contentType the content type, for example application/json, text/plain
 */
class ContentType(contentType: String) {
    internal val mediaType = contentType.toMediaType()
}
