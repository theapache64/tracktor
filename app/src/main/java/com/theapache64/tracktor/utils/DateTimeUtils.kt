package com.theapache64.tracktor.utils

import android.text.format.DateUtils
import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {

    fun toRelativeTimeString(time: Long?): String {

        if (time == null) {
            return "-"
        }

        return DateUtils.getRelativeTimeSpanString(
            time,
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        ).toString()
    }

    private val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    fun utcToLocal(createdAt: String): Long {
        return utcFormat.parse(createdAt)!!.time
    }
}


