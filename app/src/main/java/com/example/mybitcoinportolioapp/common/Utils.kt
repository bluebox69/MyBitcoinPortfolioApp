package com.example.mybitcoinportolioapp.common

import java.text.SimpleDateFormat
import java.util.*

fun Long.toReadableDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return format.format(date)
}