package com.zybooks.csc436_scheduling_app.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import com.zybooks.csc436_scheduling_app.data.local.ReminderDao
import com.zybooks.csc436_scheduling_app.data.local.SchoolClassDao
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.util.Date

class HomeScreenViewModel(private val schoolClassDao: SchoolClassDao, private val reminderDao: ReminderDao): ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun classesToday(): List<SchoolClass> {
        val today = LocalDate.now()

        val classes = schoolClassDao.getClasses().first()

        return classes.filter{ schoolClass ->
            schoolClass.days.days.contains(today.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercaseChar() })
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun remindersToday(): List<Reminder> {
        val today = LocalDate.now()

        val reminders = reminderDao.getAllReminders().first()

        return reminders
    }
}