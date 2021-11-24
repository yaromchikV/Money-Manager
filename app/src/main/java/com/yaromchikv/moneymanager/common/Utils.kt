package com.yaromchikv.moneymanager.common

import com.yaromchikv.moneymanager.R
import java.text.DecimalFormat
import kotlin.math.absoluteValue

val listOfColors = listOf(
    R.color.red,
    R.color.red_purple,
    R.color.purple,
    R.color.purple_blue,
    R.color.blue,
    R.color.blue_green,
    R.color.green,
    R.color.green_yellow,
    R.color.yellow,
    R.color.yellow_orange,
    R.color.orange,
    R.color.orange_red,
    R.color.dark_gray,
    R.color.brown,
    R.color.black
)

fun Double.toAmountFormat(): String {
    return DecimalFormat(if (this >= 0) "###,###.##" else "–###,###.##").format(this.absoluteValue)
}