package com.yaromchikv.moneymanager.feature.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.TypeConverters
import com.yaromchikv.moneymanager.common.Converter
import java.time.LocalDate

@Entity
data class DayInfo(
    @ColumnInfo(name = "date")
    val transactionDate: LocalDate,
    @ColumnInfo(name = "amount_per_day")
    val amountPerDay: Double
)
