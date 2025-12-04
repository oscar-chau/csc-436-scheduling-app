package com.zybooks.csc436_scheduling_app.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zybooks.csc436_scheduling_app.data.model.Assignment
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import com.zybooks.csc436_scheduling_app.ui.component.CalendarWidget
import com.zybooks.csc436_scheduling_app.ui.component.StatBox
import com.zybooks.csc436_scheduling_app.ui.components.AssignmentCard
import com.zybooks.csc436_scheduling_app.ui.components.ClassCard
import com.zybooks.csc436_scheduling_app.ui.components.ReminderCard
import com.zybooks.csc436_scheduling_app.ui.viewmodel.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar(vm: HomeScreenViewModel) {

    var classes by remember { mutableStateOf<List<SchoolClass>>(emptyList()) }
    var reminders by remember { mutableStateOf<List<Reminder>>(emptyList()) }
    var assignments by remember { mutableStateOf<Map<Assignment, SchoolClass>>(emptyMap()) }

    LaunchedEffect(Unit) {
        classes = vm.classesToday()
    }

    LaunchedEffect(Unit) {
        reminders = vm.remindersToday()
    }

    LaunchedEffect(Unit) {
        assignments = vm.assignmentsToday()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        // Header Box
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF736BFF), RoundedCornerShape(20.dp))
                .padding(15.dp)
        ) {
            Column(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Today, October 24",
                    fontSize = 18.sp,
                    color = Color.White
                )

                Text(
                    text = "5 tasks remaining",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(5.dp))

        // Calendar
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                CalendarWidget(
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // NEW: Today’s Schedule section (matches Home)
        Text(
            text = "Today's Schedule",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
        )

        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            // Same format as Home
            items(classes) { sc ->
                ClassCard(schoolClass = sc)
            }

            items(reminders) { rm ->
                ReminderCard(reminder = rm)
            }

            items(assignments.keys.toList()) { assignment ->
                AssignmentCard(assignment = assignment, schoolClass = assignments[assignment]!!)
            }
        }
    }
}
