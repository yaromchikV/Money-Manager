package com.yaromchikv.moneymanager.common

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalTime

class Converter {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(data: String): LocalDate {
        return LocalDate.parse(data)
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.toString()
    }

    @TypeConverter
    fun toLocalTime(data: String): LocalTime {
        return LocalTime.parse(data)
    }
}