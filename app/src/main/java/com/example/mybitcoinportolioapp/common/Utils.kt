package com.example.mybitcoinportolioapp.common

import java.text.SimpleDateFormat
import java.util.*

fun Long.toReadableDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
    return format.format(date)
}