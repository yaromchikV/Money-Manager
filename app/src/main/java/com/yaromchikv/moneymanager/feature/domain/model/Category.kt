package com.yaromchikv.moneymanager.feature.domain.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val name: String,
    val icon: Int,
    @ColumnInfo(name = "icon_color")
    val iconColor: Int
) : Parcelable
