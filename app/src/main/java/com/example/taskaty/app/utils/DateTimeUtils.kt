package com.example.taskaty.app.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeUtils {
     fun toDateFormat(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val date = inputFormat.parse(date)
        val outputDateFormat = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault())
        return outputDateFormat.format(date!!)
    }

     fun toTimeFormat(date: String): String {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS", Locale.getDefault())
        val time = inputFormat.parse(date)
        val outputTimeFormat = SimpleDateFormat("h:mm a", Locale.getDefault())
        return outputTimeFormat.format(time!!)
    }
}