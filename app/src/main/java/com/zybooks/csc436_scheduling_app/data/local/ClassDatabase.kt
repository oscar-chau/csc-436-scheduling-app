package com.zybooks.csc436_scheduling_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zybooks.csc436_scheduling_app.data.converter.DateTypeConverter
import com.zybooks.csc436_scheduling_app.data.converter.DayListTypeConverter
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass


@TypeConverters(value = [DateTypeConverter::class, DayListTypeConverter::class])
@Database(
    entities = [SchoolClass::class],
    version = 1,
    exportSchema = false
)
abstract class ClassDatabase : RoomDatabase() {
    abstract val dao: SchoolClassDao


}