package com.nchain.spvchannels.host.logging

import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import javax.inject.Inject
import javax.inject.Singleton
import okio.Buffer

@Singleton
class ObjectSerializer @Inject constructor() {
    private val moshi = Moshi.Builder()
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
