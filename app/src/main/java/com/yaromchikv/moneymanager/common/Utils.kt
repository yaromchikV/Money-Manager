package com.yaromchikv.moneymanager.common

import com.yaromchikv.moneymanager.R
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.util.*
import kotlin.math.absoluteValue

fun Double.toAmountFormat(): String {
    return DecimalFormat(if (this >= 0) "###,###.##" else "–###,###.##").format(this.absoluteValue)
}

fun getCurrentLocalDate(): LocalDate = LocalDate.parse(
    SimpleDateFormat("yyyy-MM-dd", Locale.US).format(Date())
)

fun getCurrentLocalTime(): LocalTime = LocalTime.parse(
    SimpleDateFormat("hh:mm:ss", Locale.US).format(Date())
)

fun Long.toLocalDate(): LocalDate =
    Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()

fun LocalDate.toMilliseconds(): Long = this.atStartOfDay(ZoneOffset.UTC).toInstant().toEpochMilli()

const val DAY_IN_MS: Long = 1000 * 60 * 60 * 24