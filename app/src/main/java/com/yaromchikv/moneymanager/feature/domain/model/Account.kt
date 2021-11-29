package com.yaromchikv.moneymanager.feature.domain.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "accounts")
data class Account(
    @PrimaryKey(autoGenerate = true) val id: Int? = null,
    val name: String,
    val amount: Double = 0.0,
    val currency: String = "USD",
    val color: Int
) : Parcelable