package com.zybooks.csc436_scheduling_app.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import com.zybooks.csc436_scheduling_app.navigation.AppScreen
import com.zybooks.csc436_scheduling_app.ui.components.ClassCard
import com.zybooks.csc436_scheduling_app.ui.component.QuickAddButton
import com.zybooks.csc436_scheduling_app.ui.viewmodel.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Home(vm: HomeScreenViewModel) {

    var classes by remember {
        mutableStateOf<List<SchoolClass>>(emptyList())
    }

    LaunchedEffect(Unit) {
        classes = vm.classesToday()
    }

    Column(modifier = Modifier.fillMaxSize()) {

        // Title
        Text(
            AppScreen.HOME.title,
            textAlign = TextAlign.Center,
            fontSize = 40.sp,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )

        // Classes list
        LazyColumn(
            modifier = Modifier
                .weight(1f)   // ← Ensures the buttons stay at the bottom
                .fillMaxWidth()
        ) {
            items(classes) { sc ->
                ClassCard(schoolClass = sc)
            }
        }
        // Title
        Row {
            Text(
                "Quick Add",
                fontSize = 20.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
            )
        }

        // Quick add buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            QuickAddButton(
                icon = Icons.Default.Star,
                label = "Class",
                onClick = { /* TODO: navigate to add class */ }
            )

            QuickAddButton(
                icon = Icons.Default.List,
                label = "Task",
                onClick = { /* TODO: navigate to add task */ }
            )

            QuickAddButton(
                icon = Icons.Default.Notifications,
                label = "Reminder",
                onClick = { /* TODO: navigate to add reminder */ }
            )
        }
    }
}
