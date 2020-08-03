package com.pavesid.subsmanager.utils

import android.content.Context
import android.text.format.DateUtils
import android.widget.TextView
import androidx.annotation.MainThread
import java.util.Calendar

@MainThread
fun TextView.setInitialDateTime(context: Context, dateAndTime: Long?) {
    this.text = DateUtils.formatDateTime(
        context,
        dateAndTime ?: Calendar.getInstance().timeInMillis,
        DateUtils.FORMAT_SHOW_DATE or DateUtils.FORMAT_SHOW_YEAR
    )
}

@MainThread
fun Calendar.clearHours(): Calendar {
    this.apply {
        clear(Calendar.HOUR_OF_DAY)
        clear(Calendar.MINUTE)
        clear(Calendar.SECOND)
        clear(Calendar.MILLISECOND)
    }
    return this
}