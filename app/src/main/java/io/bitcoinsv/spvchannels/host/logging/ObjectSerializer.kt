
// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.host.logging

import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import io.bitcoinsv.spvchannels.datetime.IsoDateTimeConverter
import javax.inject.Inject
import javax.inject.Singleton
import okio.Buffer

@Singleton
class ObjectSerializer @Inject constructor() {
    private val moshi = Moshi.Builder()
        .add(IsoDateTimeConverter())
        .add(MessageAdapter())
        .build()

    fun <T> logTyped(value: T?, clazz: Class<T>, pretty: Boolean): String? {
        val adapter = moshi.adapter(clazz)
        if (!pretty) {
            return adapter.toJson(value)
        }

        val buffer = Buffer()
        val writer = JsonWriter.of(buffer)
        writer.indent = "\t"
        adapter.toJson(writer, value)
        return buffer.readUtf8()
    }

    inline fun <reified Type> serialize(value: Type?, pretty: Boolean = true): String? {
        return logTyped(value, Type::class.java, pretty)
    }
}
