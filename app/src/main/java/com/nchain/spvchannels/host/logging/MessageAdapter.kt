package com.nchain.spvchannels.host.logging

import com.nchain.spvchannels.encryption.RawMessage
import com.squareup.moshi.FromJson
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.ToJson
import java.nio.charset.StandardCharsets
import java.util.Calendar

class MessageAdapter {
    @ToJson
    fun toJson(writer: JsonWriter, value: RawMessage?, calendarAdapter: JsonAdapter<Calendar>) {
        if (value == null) {
            writer.jsonValue(null)
            return
        }

        writer.beginObject()
        writer.name("sequence")
        writer.value(value.sequence)
        writer.name("received")
        writer.jsonValue(calendarAdapter.toJson(value.date))
        writer.name("content_type")
        writer.value(value.contentType)
        writer.name("payload")
        writer.value(value.payload?.toString(StandardCharsets.UTF_8))
        writer.endObject()
    }

    @FromJson
    fun fromJson(@Suppress("UNUSED_PARAMETER") ignored: JsonReader): RawMessage? {
        throw UnsupportedOperationException()
    }
}
