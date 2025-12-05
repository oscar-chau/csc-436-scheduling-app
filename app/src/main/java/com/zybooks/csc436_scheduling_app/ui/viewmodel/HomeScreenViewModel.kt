package com.zybooks.csc436_scheduling_app.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zybooks.csc436_scheduling_app.data.local.AssignmentDao
import com.zybooks.csc436_scheduling_app.data.local.ReminderDao
import com.zybooks.csc436_scheduling_app.data.local.SchoolClassDao
import com.zybooks.csc436_scheduling_app.data.model.Assignment
import com.zybooks.csc436_scheduling_app.data.model.DayList
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.util.Date

class HomeScreenViewModel(
private val schoolClassDao: SchoolClassDao,
private val reminderDao: ReminderDao,
private val assignmentDao: AssignmentDao
) : ViewModel() {
    val allClasses: StateFlow<List<SchoolClass>> =
        schoolClassDao.getClasses()
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun addClass(
        name: String,
        location: String?,
        startDate: Date?,
        endDate: Date?,
        startTime: Date?,
        endTime: Date?,
        days: DayList
    ) {
        val schoolClass = SchoolClass(
            name = name,
            location = location,
            startDate = startDate ?: Date(),
            endDate = endDate ?: Date(),
            startTime = startTime ?: Date(),
            endTime = endTime ?: Date(),
            days = days
        )

        viewModelScope.launch {
            schoolClassDao.upsertClass(schoolClass)
        }
    }

    fun addTask(
        name: String,
        dueDate: Date?,
        dueTime: Date?,
        classId: Int?
    ) {
        val assignment = Assignment(
            title = name,
            date = dueDate ?: Date(),
            time = dueTime ?: Date(),
            classId = classId ?: 0
        )

        viewModelScope.launch {
            assignmentDao.upsertAssignment(assignment)
        }
    }

    fun addReminder(
        name: String,
        location: String?,
        date: Date?,
        time: Date?
    ) {
        val reminder = Reminder(
            title = name,
            location = location ?: "",
            date = date ?: Date(),
            time = time ?: Date()
        )

        viewModelScope.launch {
            reminderDao.upsertReminder(reminder)
        }
    }

    // -------------------------
    // The rest of your flows...
    // -------------------------

    @RequiresApi(Build.VERSION_CODES.O)
    val classesToday: StateFlow<List<SchoolClass>> = schoolClassDao.getClasses()
        .map { classes ->
            val today = LocalDate.now()
            val todayFullName = today.dayOfWeek
                .name.lowercase()
                .replaceFirstChar { it.uppercaseChar() }

            val dayMap = mapOf(
                "Mon" to "Monday",
                "Tue" to "Tuesday",
                "Wed" to "Wednesday",
                "Thu" to "Thursday",
                "Fri" to "Friday",
                "Sat" to "Saturday",
                "Sun" to "Sunday"
            )

            classes.filter { schoolClass ->
                val converted = schoolClass.days.days.map { dayMap[it] ?: it }
                converted.contains(todayFullName) &&
                        schoolClass.startDate <= Date() &&
                        schoolClass.endDate >= Date()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    @RequiresApi(Build.VERSION_CODES.O)
    val remindersToday: StateFlow<List<Reminder>> =
        reminderDao.getAllReminders()
            .map { reminders ->
                val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val todayString = format.format(Date())
                reminders.filter { reminder ->
                    format.format(reminder.date) == todayString
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())


    @RequiresApi(Build.VERSION_CODES.O)
    val assignmentsToday: StateFlow<Map<Assignment, SchoolClass>> =
        assignmentDao.getAllAssignments()
            .map { assignments ->
                val format = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val todayString = format.format(Date())
                assignments.filter { assignment ->
                    format.format(assignment.date) == todayString
                }
            }
            .combine(schoolClassDao.getClasses()) { assignments, classes ->
                val classMap = classes.associateBy { it.id }
                assignments.mapNotNull { assignment ->
                    classMap[assignment.classId]?.let { schoolClass ->
                        assignment to schoolClass
                    }
                }.toMap()
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}
