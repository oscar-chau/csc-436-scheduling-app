package com.zybooks.csc436_scheduling_app.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.google.gson.Gson
import java.util.Date

@Entity(tableName = "class")
data class SchoolClass(
    val name: String,
    val location: String?,
    val startDate: Date,
    val endDate: Date,
    val startTime: Date,
    val endTime: Date,
    val days: DayList,
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0
)

data class DayList(
    val days: List<String>
)