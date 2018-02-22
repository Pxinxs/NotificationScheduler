package com.test.scheduler.utils

import java.text.SimpleDateFormat
import java.util.*

class DateUtils {

    companion object {
        private const val DATE_FORMAT_PATTERN = "dd-MM-yyyy"
        private const val TIME_FORMAT_PATTERN = "HH:mm"
    }

    fun getDate(calendar: Calendar): String {
        val format = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale("ru"))
        return format.format(calendar.time)
    }

    fun getTime(calendar: Calendar): String {
        val format = SimpleDateFormat(TIME_FORMAT_PATTERN, Locale("ru"))
        return format.format(calendar.time)
    }

}