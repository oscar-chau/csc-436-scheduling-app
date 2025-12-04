package com.zybooks.csc436_scheduling_app.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import com.zybooks.csc436_scheduling_app.ui.components.ClassCard
import com.zybooks.csc436_scheduling_app.ui.component.AddItemDialog
import com.zybooks.csc436_scheduling_app.ui.component.QuickAddButton
import com.zybooks.csc436_scheduling_app.ui.component.StatBox
import com.zybooks.csc436_scheduling_app.ui.components.ReminderCard
import com.zybooks.csc436_scheduling_app.ui.viewmodel.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(vm: HomeScreenViewModel) {

    var classes by remember { mutableStateOf<List<SchoolClass>>(emptyList()) }
    var reminders by remember { mutableStateOf<List<Reminder>>(emptyList()) }

    // Popup dialog states
    var showAddClass by remember { mutableStateOf(false) }
    var showAddTask by remember { mutableStateOf(false) }
    var showAddReminder by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        classes = vm.classesToday()
    }

    LaunchedEffect(Unit) {
        reminders = vm.remindersToday()
    }

    val classCount = classes.size
    val taskCount = 4          // TODO pull real data
    val reminderCount = reminders.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Column(
            // Header Section
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFFEDEBFE), shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp))
                .padding(20.dp)
        ) {
            Text(
                text = "Today",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatBox("Classes", classCount)
                StatBox("Tasks", taskCount)
                StatBox("Reminders", reminderCount)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Classes + Reminders Today List
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            items(classes) { sc ->
                ClassCard(schoolClass = sc)
            }

            items(reminders) { rm ->
                ReminderCard(reminder = rm)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Quick Add",
                fontSize = 20.sp,
                modifier = Modifier.padding(start = 4.dp, top = 10.dp, bottom = 10.dp)
            )
        }

        // Quick Add Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickAddButton(
                icon = Icons.Default.Star,
                label = "Class",
                onClick = { showAddClass = true }
            )

            QuickAddButton(
                icon = Icons.AutoMirrored.Filled.List,
                label = "Task",
                onClick = { showAddTask = true }
            )

            QuickAddButton(
                icon = Icons.Default.Notifications,
                label = "Reminder",
                onClick = { showAddReminder = true }
            )
        }
    }

    if (showAddClass) {
        AddItemDialog(
            title = "Add Class",
            onDismiss = { showAddClass = false }
        ) { name, location, startDate, endDate, startTime, endTime, days ->
            // TODO: Save class
            println("CLASS ADDED → $name @ $location [$days]")
        }
    }

    if (showAddTask) {
        AddItemDialog(
            title = "Add Task",
            onDismiss = { showAddTask = false }
        ) { name, location, startDate, endDate, startTime, endTime, days ->
            // TODO: Save task
            println("TASK ADDED → $name")
        }
    }

    if (showAddReminder) {
        AddItemDialog(
            title = "Add Reminder",
            onDismiss = { showAddReminder = false }
        ) { name, location, startDate, endDate, startTime, endTime, days ->
            // TODO: Save reminder
            println("REMINDER ADDED → $name on $startDate")
        }
    }
}
