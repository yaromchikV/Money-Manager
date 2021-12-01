package com.yaromchikv.moneymanager.feature.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.yaromchikv.moneymanager.common.Converter
import com.yaromchikv.moneymanager.common.getCurrentLocalDate
import com.yaromchikv.moneymanager.common.getCurrentLocalTime
import java.time.LocalDate
import java.time.LocalTime

@TypeConverters(Converter::class)
data class TransactionView(
    val id: Int,
    val note: String,
    val amount: Double,
    val date: LocalDate,
    val time: LocalTime,
    @ColumnInfo(name = "category_name")
    val categoryName: String,
    @ColumnInfo(name = "account_name")
    val accountName: String,
    val icon: Int,
    @ColumnInfo(name = "icon_color")
    val iconColor: Int
)