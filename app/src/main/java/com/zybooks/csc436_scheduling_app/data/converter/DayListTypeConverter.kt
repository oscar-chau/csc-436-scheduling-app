package com.zybooks.csc436_scheduling_app.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.zybooks.csc436_scheduling_app.data.model.DayList

class DayListTypeConverter {
    @TypeConverter
    fun convertDayListToJSONString(dayList: DayList): String = Gson().toJson(dayList)
    @TypeConverter
    fun convertJSONStringToDayList(jsonString: String): DayList? = Gson().fromJson(jsonString, DayList::class.java)
}