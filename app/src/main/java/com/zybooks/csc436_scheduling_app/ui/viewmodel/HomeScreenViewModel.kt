package com.zybooks.csc436_scheduling_app.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zybooks.csc436_scheduling_app.data.local.AssignmentDao
import com.zybooks.csc436_scheduling_app.data.local.ReminderDao
import com.zybooks.csc436_scheduling_app.data.local.SchoolClassDao
import com.zybooks.csc436_scheduling_app.data.model.Assignment
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.util.Date

class HomeScreenViewModel(
    private val schoolClassDao: SchoolClassDao,
    private val reminderDao: ReminderDao,
    private val assignmentDao: AssignmentDao
) : ViewModel() {

    @RequiresApi(Build.VERSION_CODES.O)
    val classesToday: StateFlow<List<SchoolClass>> = schoolClassDao.getClasses()
        .map { classes ->
            val today = LocalDate.now()
            classes.filter { schoolClass ->
                schoolClass.days.days.contains(today.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercaseChar() }) && schoolClass.startDate <= Date() && schoolClass.endDate >= Date()
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @RequiresApi(Build.VERSION_CODES.O)
    val remindersToday: StateFlow<List<Reminder>> = reminderDao.getAllReminders()
        .map { reminders ->
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val todayString = dateFormat.format(Date())
            reminders.filter { reminder ->
                val reminderString = dateFormat.format(reminder.date)
                reminderString == todayString
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    @RequiresApi(Build.VERSION_CODES.O)
    val assignmentsToday: StateFlow<Map<Assignment, SchoolClass>> =
        assignmentDao.getAllAssignments()
            .map { assignments ->
                val dateFormat =
                    java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
                val todayString = dateFormat.format(Date())
                assignments.filter { assignment ->
                    val assignmentDate = dateFormat.format(assignment.date)
                    assignmentDate == todayString
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