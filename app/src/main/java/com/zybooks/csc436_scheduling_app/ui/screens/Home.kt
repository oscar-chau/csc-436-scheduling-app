package com.zybooks.csc436_scheduling_app.ui.screens

import android.graphics.Color.rgb
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.zybooks.csc436_scheduling_app.ui.component.AddItemDialog
import com.zybooks.csc436_scheduling_app.ui.component.QuickAddButton
import com.zybooks.csc436_scheduling_app.ui.component.StatBox
import com.zybooks.csc436_scheduling_app.ui.components.AssignmentCard
import com.zybooks.csc436_scheduling_app.ui.components.ClassCard
import com.zybooks.csc436_scheduling_app.ui.components.ReminderCard
import com.zybooks.csc436_scheduling_app.ui.viewmodel.HomeScreenViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(vm: HomeScreenViewModel) {

    val classes by vm.classesToday.collectAsStateWithLifecycle()
    val reminders by vm.remindersToday.collectAsStateWithLifecycle()
    val assignments by vm.assignmentsToday.collectAsStateWithLifecycle()

    // Popup dialog states
    var showAddClass by remember { mutableStateOf(false) }
    var showAddTask by remember { mutableStateOf(false) }
    var showAddReminder by remember { mutableStateOf(false) }

    val classCount = classes.size
    val taskCount = assignments.size
    val reminderCount = reminders.size

    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMM d',' yyyy") // e.g., "Oct 4, 2024"
    val formattedDate = today.format(formatter)

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
            .fillMaxSize()
            .padding(20.dp),
    ) {

        Surface(
            shadowElevation = 2.dp,
            shape = RoundedCornerShape(12.dp),
            color = Color.White
        ) {
            Column(
                // Header Section
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = androidx.compose.foundation.shape.RoundedCornerShape(24.dp)
                    )
                    .padding(20.dp)
            ) {
                Row(
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()

                ) {
                    Text(
                        text = "Today",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(rgb(29, 31, 45)) // basically black
                    )

                    Text(
                        text = formattedDate,
                        fontSize = 16.sp,
                        color = Color.Gray
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)

                ) {
                    StatBox(
                        "Classes",
                        classCount,
                        Color(rgb(216, 236, 255)),
                        Color(rgb(38, 101, 232)),
                        Modifier.weight(1f)
                    )

                    StatBox(
                        "Tasks",
                        taskCount,
                        Color(rgb(240, 253, 244)),
                        Color(rgb(75, 174, 105)),
                        Modifier.weight(1f)
                    )
                    StatBox(
                        "Reminders",
                        reminderCount,
                        Color(rgb(251, 245, 255)),
                        Color(rgb(149, 54, 237)),
                        Modifier.weight(1f)
                    )
                }
            }
        }

        if (!(classes.isEmpty() && reminders.isEmpty() && assignments.isEmpty())) {
            Text(
                text = "Upcoming",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 13.dp, top = 12.dp, bottom = 5.dp)
            )

            // Classes + Reminders Today List
            classes.forEach { sc ->
                ClassCard(schoolClass = sc)
            }
            reminders.forEach { rm ->
                ReminderCard(reminder = rm)
            }
            assignments.keys.toList().forEach { assignment ->
                AssignmentCard(assignment = assignment, schoolClass = assignments[assignment]!!)
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = "Quick Add",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 13.dp, top = 12.dp, bottom = 5.dp)
            )
        }

        // Quick Add Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            QuickAddButton(
                icon = Icons.Default.Star,
                label = "Class",
                onClick = { showAddClass = true },
                iconColor = Color(rgb(38, 101, 232)),
                outerColor = Color(rgb(216, 236, 255))
            )

            Spacer(modifier = Modifier.width(20.dp))

            QuickAddButton(
                icon = Icons.AutoMirrored.Filled.List,
                label = "Task",
                onClick = { showAddTask = true },
                iconColor = Color(rgb(75, 174, 105)),
                outerColor = Color(rgb(240, 253, 244))
            )

            Spacer(modifier = Modifier.width(20.dp))

            QuickAddButton(
                icon = Icons.Default.Notifications,
                label = "Reminder",
                onClick = { showAddReminder = true },
                iconColor = Color(rgb(149, 54, 237)),
                outerColor = Color(rgb(251, 245, 255))
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