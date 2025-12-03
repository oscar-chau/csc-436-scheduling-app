package com.zybooks.csc436_scheduling_app.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.zybooks.csc436_scheduling_app.data.converter.DateTypeConverter
import com.zybooks.csc436_scheduling_app.data.converter.DayListTypeConverter
import com.zybooks.csc436_scheduling_app.data.model.Assignment
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass


@TypeConverters(value = [DateTypeConverter::class, DayListTypeConverter::class])
@Database(
    entities = [SchoolClass::class, Reminder::class, Assignment::class],
    version = 3,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract val schoolClassDao: SchoolClassDao
    abstract val reminderDao: ReminderDao
    abstract val assignmentDao: AssignmentDao

}