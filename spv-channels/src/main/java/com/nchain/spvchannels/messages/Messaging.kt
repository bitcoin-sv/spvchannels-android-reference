package com.nchain.spvchannels.messages

import kotlin.coroutines.CoroutineContext

class Messaging internal constructor(
    private val messageService: MessageService,
    private val token: String,
    private val context: CoroutineContext,
)
