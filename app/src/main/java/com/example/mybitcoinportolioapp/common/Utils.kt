package com.example.mybitcoinportolioapp.common

import java.text.SimpleDateFormat
import java.util.*
import android.content.Context
import android.widget.Toast

fun Long.toReadableDate(): String {
    val date = Date(this)
    val format = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())
    return format.format(date)
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}