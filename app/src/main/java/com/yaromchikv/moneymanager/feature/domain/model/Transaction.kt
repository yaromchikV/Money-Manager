package com.yaromchikv.moneymanager.feature.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.CASCADE
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.yaromchikv.moneymanager.common.Converter
import com.yaromchikv.moneymanager.common.getCurrentLocalDate
import com.yaromchikv.moneymanager.common.getCurrentLocalTime
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "transactions",
    foreignKeys = [
        ForeignKey(
            entity = Category::class,
            parentColumns = ["id"],
            childColumns = ["category_id"],
            onDelete = CASCADE
        ),
        ForeignKey(
            entity = Account::class,
            parentColumns = ["id"],
            childColumns = ["account_id"],
            onDelete = CASCADE
        )
    ]
)
@TypeConverters(Converter::class)
data class Transaction(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val note: String,
    val amount: Double,
    val date: LocalDate = getCurrentLocalDate(),
    val time: LocalTime = getCurrentLocalTime(),
    @ColumnInfo(name = "account_id")
    val accountId: Int,
    @ColumnInfo(name = "category_id")
    val categoryId: Int
)