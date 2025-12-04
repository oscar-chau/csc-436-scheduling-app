package com.zybooks.csc436_scheduling_app.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.text.capitalize
import androidx.lifecycle.ViewModel
import com.zybooks.csc436_scheduling_app.data.local.AssignmentDao
import com.zybooks.csc436_scheduling_app.data.local.ReminderDao
import com.zybooks.csc436_scheduling_app.data.local.SchoolClassDao
import com.zybooks.csc436_scheduling_app.data.model.Assignment
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import kotlinx.coroutines.flow.first
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

class HomeScreenViewModel(private val schoolClassDao: SchoolClassDao, private val reminderDao: ReminderDao, private val assignmentDao: AssignmentDao): ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun classesToday(): List<SchoolClass> {
        val today = LocalDate.now()

        val classes = schoolClassDao.getClasses().first()

        return classes.filter{ schoolClass ->
            schoolClass.days.days.contains(today.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercaseChar() }) && schoolClass.startDate <= Date() && schoolClass.endDate >= Date()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun remindersToday(): List<Reminder> {
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val todayString = dateFormat.format(Date())

        val reminders = reminderDao.getAllReminders().first()

        return reminders.filter { reminder ->
            val reminderString = dateFormat.format(reminder.date)
            reminderString == todayString
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    suspend fun assignmentsToday(): Map<Assignment, SchoolClass> {
        val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
        val todayString = dateFormat.format(Date())

        val assignments = assignmentDao.getAllAssignments().first().filter { assignment ->
            val assignmentDate =  dateFormat.format(assignment.date)
            assignmentDate == todayString
        }

        val fin: MutableMap<Assignment, SchoolClass> = emptyMap<Assignment, SchoolClass>().toMutableMap()

        assignments.forEach { it ->
            val schoolClass = schoolClassDao.getClassById(it.classId)
            fin[it] = schoolClass
        }

        return fin
    }

}