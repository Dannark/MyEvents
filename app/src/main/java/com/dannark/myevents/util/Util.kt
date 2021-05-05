package com.dannark.myevents.util

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo
import java.text.NumberFormat
import java.util.concurrent.TimeUnit

private val PUNCTUATION = listOf(", ", "; ", ": ", " ")

fun Long.getCurrency():String{
    val format: NumberFormat = NumberFormat.getCurrencyInstance()
    format.setMaximumFractionDigits(2)
    return format.format(this)
}

/**
 * Truncate long text with a preference for word boundaries and without trailing punctuation.
 */
fun String.smartTruncate(length: Int): String {
    val words = split(" ")
    var added = 0
    var hasMore = false
    val builder = StringBuilder()
    for (word in words) {
        if (builder.length > length) {
            hasMore = true
            break
        }
        builder.append(word)
        builder.append(" ")
        added += 1
    }

    PUNCTUATION.map {
        if (builder.endsWith(it)) {
            builder.replace(builder.length - it.length, builder.length, "")
        }
    }

    if (hasMore) {
        builder.append("...")
    }
    return builder.toString()
}

fun isConnectedToInternet(application: Application): Boolean{
    val cm = application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
    val isConnected: Boolean = activeNetwork?.isConnected == true

    return isConnected
}

fun getTimeArrayDiff(past: Long): TimeObject {
    val diffInMillisec = System.currentTimeMillis() - past

    val days = TimeUnit.MILLISECONDS.toDays(diffInMillisec)
    val months = days / 30
    val years = months / 12

    return TimeObject(
            years,
            months,
            days,
            TimeUnit.MILLISECONDS.toHours(diffInMillisec),
            TimeUnit.MILLISECONDS.toMinutes(diffInMillisec),
            TimeUnit.MILLISECONDS.toSeconds(diffInMillisec)
    )
}

data class TimeObject(
        val year: Long,
        val month: Long,
        val days: Long,
        val hours: Long,
        val min: Long,
        val sec: Long,
)