// Copyright (c) 2021 Bitcoin Association.
// Distributed under the Open BSV software license, see the accompanying file LICENSE
package io.bitcoinsv.spvchannels.datetime

import java.util.Calendar
import org.junit.Assert.assertEquals
import org.junit.Test

class IsoDateTimeConverterTest {
    private val sampleDate = "2021-03-10T07:51:32.748Z"
    private val formatter = IsoDateTimeConverter()

    @Test
    fun toDateParsesSuccessfully() {
        val date = formatter.dateFromJson(sampleDate)

        assertEquals(2021, date.get(Calendar.YEAR))
        assertEquals(2, date.get(Calendar.MONTH))
        assertEquals(10, date.get(Calendar.DAY_OF_MONTH))
        assertEquals(7, date.get(Calendar.HOUR_OF_DAY))
        assertEquals(51, date.get(Calendar.MINUTE))
        assertEquals(32, date.get(Calendar.SECOND))
        assertEquals(748, date.get(Calendar.MILLISECOND))
    }

    @Test
    fun toStringFormatsCorrectly() {
        val date = Calendar.getInstance().apply {
            set(Calendar.YEAR, 2021)
            set(Calendar.MONTH, 2)
            set(Calendar.DAY_OF_MONTH, 10)
            set(Calendar.HOUR_OF_DAY, 7)
            set(Calendar.MINUTE, 51)
            set(Calendar.SECOND, 32)
            set(Calendar.MILLISECOND, 748)
        }
        val formatted = formatter.dateToJson(date)
        assertEquals(sampleDate, formatted)
    }
}
