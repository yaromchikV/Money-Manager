package com.yaromchikv.moneymanager.feature.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class CategoryView(
    val id: Int,
    val name: String,
    val icon: Int,
    @ColumnInfo(name = "icon_color")
    val iconColor: String,
    @ColumnInfo(name = "category_amount")
    val amount: Double
) : Parcelable
