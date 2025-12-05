package com.zybooks.csc436_scheduling_app.ui.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.zybooks.csc436_scheduling_app.data.local.AssignmentDao
import com.zybooks.csc436_scheduling_app.data.local.ReminderDao
import com.zybooks.csc436_scheduling_app.data.local.SchoolClassDao

class ViewModelFactory(
    private val schoolClassDao: SchoolClassDao,
    private val reminderDao: ReminderDao,
    private val assignmentDao: AssignmentDao
) : ViewModelProvider.Factory {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeScreenViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeScreenViewModel(schoolClassDao, reminderDao, assignmentDao) as T
        }
        if (modelClass.isAssignableFrom(CalendarViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return CalendarViewModel(schoolClassDao, reminderDao, assignmentDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
