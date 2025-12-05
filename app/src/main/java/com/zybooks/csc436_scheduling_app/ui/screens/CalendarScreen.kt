package com.zybooks.csc436_scheduling_app.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zybooks.csc436_scheduling_app.ui.component.CalendarWidget
import com.zybooks.csc436_scheduling_app.ui.components.AssignmentCard
import com.zybooks.csc436_scheduling_app.ui.components.ClassCard
import com.zybooks.csc436_scheduling_app.ui.components.ReminderCard
import com.zybooks.csc436_scheduling_app.ui.viewmodel.CalendarViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CalendarScreen(vm: CalendarViewModel) {
    val selectedDate by vm.selectedDate.collectAsStateWithLifecycle()
    val classes by vm.classesForSelectedDate.collectAsStateWithLifecycle()
    val reminders by vm.remindersForSelectedDate.collectAsStateWithLifecycle()
    val assignments by vm.assignmentsForSelectedDate.collectAsStateWithLifecycle()

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        CalendarWidget(onDateSelected = {
            vm.onDateSelected(it)
        })

        // Display events for the selected date
        classes.forEach { schoolClass ->
            ClassCard(schoolClass = schoolClass)
        }
        reminders.forEach { reminder ->
            ReminderCard(reminder = reminder)
        }
        assignments.keys.forEach { assignment ->
            AssignmentCard(assignment = assignment, schoolClass = assignments[assignment]!!)
        }
    }
}