package com.zybooks.csc436_scheduling_app.data.converter

import androidx.room.TypeConverter
import java.util.Date

class DateTypeConverter {
    @TypeConverter
    fun convertDateToLong(date: Date): Long = date.getTime()
    @TypeConverter
    fun convertLongToDate(time: Long): Date = Date(time)
}