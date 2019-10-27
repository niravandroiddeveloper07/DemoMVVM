package com.example.demomvvm.util

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Date

/**
 * Util class for Date manipulations
 */
object DateUtil {

    private const val DATE_FORMAT_YYYYMMDD = "yyyy/MM/dd"
    private const val DATE_TIME_FORMAT_YYYYMMDD_HHMM = "yyyy/MM/dd HH:mm"

    @Throws(ParseException::class)
    fun createDateFromString(date: String?, format: String): Date? {
        requireNotNull(date) { "Date is null!" }
        val df = SimpleDateFormat(format)
        return df.parse(date)
    }

    @JvmOverloads
    fun formatDate(date: Date, format: String = DATE_FORMAT_YYYYMMDD): String {
        val sdf = SimpleDateFormat(format)
        return sdf.format(date)
    }

    /**
     * Formats Date with Time using yyyy/MM/dd HH:mm
     *
     * @return formatted Date as a String
     */
    fun formatDateTime(date: Date): String {
        return formatDate(date, DATE_TIME_FORMAT_YYYYMMDD_HHMM)
    }

}
