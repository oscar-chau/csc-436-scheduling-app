package com.zybooks.csc436_scheduling_app.ui.screens

import android.graphics.Color.rgb
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.zybooks.csc436_scheduling_app.data.model.DayList
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(vm: HomeScreenViewModel) {

    // Today-only data for "Upcoming" section
    val todayClasses by vm.classesToday.collectAsStateWithLifecycle()
    val reminders by vm.remindersToday.collectAsStateWithLifecycle()
    val assignments by vm.assignmentsToday.collectAsStateWithLifecycle()

    // All classes (for Add Task dialog)
    val allClasses by vm.allClasses.collectAsStateWithLifecycle()

    // Dialog states
    var showAddClass by remember { mutableStateOf(false) }
    var showAddTask by remember { mutableStateOf(false) }
    var showAddReminder by remember { mutableStateOf(false) }

    val classCount = todayClasses.size
    val taskCount = assignments.size
    val reminderCount = reminders.size

    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("MMM d',' yyyy")
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
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, shape = RoundedCornerShape(24.dp))
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
                        color = Color(rgb(29, 31, 45))
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

        // =============================
        // UPCOMING SECTION
        // =============================
        if (!(todayClasses.isEmpty() && reminders.isEmpty() && assignments.isEmpty())) {
            Text(
                text = "Upcoming",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(start = 14.dp, top = 12.dp, bottom = 5.dp)
            )

            todayClasses.forEach { sc ->
                ClassCard(schoolClass = sc)
            }

            reminders.forEach { rm ->
                ReminderCard(reminder = rm)
            }

            assignments.keys.forEach { assignment ->
                AssignmentCard(
                    assignment = assignment,
                    schoolClass = assignments[assignment]!!
                )
            }
        }

        // =============================
        // QUICK ADD
        // =============================
        Text(
            text = "Quick Add",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 14.dp, top = 12.dp, bottom = 5.dp)
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
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

    // =============================
    // DIALOGS
    // =============================

    // ADD CLASS
    if (showAddClass) {
        AddItemDialog(
            title = "Add Class",
            onDismiss = { showAddClass = false }
        ) { name, location, startDate, endDate, startTime, endTime, days, _ ->

            vm.addClass(
                name = name,
                location = location,
                startDate = startDate,
                endDate = endDate,
                startTime = startTime,
                endTime = endTime,
                days = DayList(days)
            )

            showAddClass = false
        }
    }

    // ADD TASK — now uses ALL classes
    if (showAddTask) {
        AddItemDialog(
            title = "Add Task",
            classes = allClasses,   // <-- UPDATED HERE
            onDismiss = { showAddTask = false }
        ) { name, _, dueDate, _, dueTime, _, _, classId ->

            vm.addTask(
                name = name,
                dueDate = dueDate,
                dueTime = dueTime,
                classId = classId?.toInt()
            )

            showAddTask = false
        }
    }

    // ADD REMINDER
    if (showAddReminder) {
        AddItemDialog(
            title = "Add Reminder",
            onDismiss = { showAddReminder = false }
        ) { name, location, date, _, time, _, _, _ ->

            vm.addReminder(
                name = name,
                location = location,
                date = date,
                time = time
            )

            showAddReminder = false
        }
    }
}
