package com.zybooks.csc436_scheduling_app.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {
    @Upsert
    suspend fun upsertReminder(reminder: Reminder)

    @Delete
    suspend fun deleteReminder(reminder: Reminder)

    @Query("DELETE FROM reminder")
    suspend fun deleteAllReminders()

    @Query("SELECT * FROM reminder")
     fun getAllReminders(): Flow<List<Reminder>>
}