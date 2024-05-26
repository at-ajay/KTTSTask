package com.kttstask.util

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.annotation.RequiresApi

@SuppressLint("SimpleDateFormat")
@RequiresApi(Build.VERSION_CODES.N)
fun getFormattedDate(timeInMillis: Long): String {
    val dateFormatter = SimpleDateFormat("dd/MM/yyyy | hh:mm:ss")
    val calendar = Calendar.getInstance()
    calendar.timeInMillis = timeInMillis
    return dateFormatter.format(calendar)
}