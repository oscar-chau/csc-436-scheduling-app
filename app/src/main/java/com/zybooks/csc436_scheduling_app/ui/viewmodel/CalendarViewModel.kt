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
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date

@RequiresApi(Build.VERSION_CODES.O)
class CalendarViewModel(
    private val schoolClassDao: SchoolClassDao,
    private val reminderDao: ReminderDao,
    private val assignmentDao: AssignmentDao
) : ViewModel() {

    private val _selectedDate = MutableStateFlow(LocalDate.now())
    val selectedDate: StateFlow<LocalDate> = _selectedDate

    fun onDateSelected(date: LocalDate) {
        _selectedDate.value = date
    }

    val classesForSelectedDate: StateFlow<List<SchoolClass>> =
        combine(schoolClassDao.getClasses(), selectedDate) { classes, date ->
            val dateAsDate = Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant())
            classes.filter { schoolClass ->
                schoolClass.days.days.contains(date.dayOfWeek.name.lowercase().replaceFirstChar { it.uppercaseChar() }) &&
                        !schoolClass.startDate.after(dateAsDate) && !schoolClass.endDate.before(dateAsDate)
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val remindersForSelectedDate: StateFlow<List<Reminder>> =
        combine(reminderDao.getAllReminders(), selectedDate) { reminders, date ->
            val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val selectedDateString = dateFormat.format(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))
            reminders.filter { reminder ->
                val reminderString = dateFormat.format(reminder.date)
                reminderString == selectedDateString
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val assignmentsForSelectedDate: StateFlow<Map<Assignment, SchoolClass>> =
        combine(
            assignmentDao.getAllAssignments(),
            schoolClassDao.getClasses(),
            selectedDate
        ) { assignments, classes, date ->
            val dateFormat =
                java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.getDefault())
            val selectedDateString = dateFormat.format(Date.from(date.atStartOfDay(ZoneId.systemDefault()).toInstant()))

            val assignmentsForDate = assignments.filter { assignment ->
                val assignmentDate = dateFormat.format(assignment.date)
                assignmentDate == selectedDateString
            }

            val classMap = classes.associateBy { it.id }
            assignmentsForDate.mapNotNull { assignment ->
                classMap[assignment.classId]?.let { schoolClass ->
                    assignment to schoolClass
                }
            }.toMap()
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())
}