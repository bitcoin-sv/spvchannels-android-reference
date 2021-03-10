package com.nchain.spvchannels.datetime

import android.annotation.SuppressLint
import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.text.SimpleDateFormat
import java.util.Calendar

/**
 * Used to convert between string and Date representation, using ISO date format.
 */
class IsoDateTimeConverter {
    /*
     We're using this format in transport layer only, and as such, it's not subject to
     locale specific formats.
    */
    @SuppressLint("SimpleDateFormat")
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")

    @FromJson
    fun dateFromJson(date: String): Calendar {
        // Method throws if date could not be parsed, null cannot be returned
        val calendar = Calendar.getInstance()
        calendar.time = dateFormat.parse(date)!!
        return calendar
    }

    @ToJson
    fun dateToJson(date: Calendar): String {
        return dateFormat.format(date.time)
    }
}
