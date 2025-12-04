package com.zybooks.csc436_scheduling_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.zybooks.csc436_scheduling_app.data.local.AppDatabase
import com.zybooks.csc436_scheduling_app.data.model.Assignment
import com.zybooks.csc436_scheduling_app.data.model.DayList
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import com.zybooks.csc436_scheduling_app.navigation.BottomNavBar
import com.zybooks.csc436_scheduling_app.navigation.NavGraph
import com.zybooks.csc436_scheduling_app.ui.theme.Csc436schedulingappTheme
import com.zybooks.csc436_scheduling_app.ui.viewmodel.HomeScreenViewModel
import com.zybooks.csc436_scheduling_app.ui.viewmodel.ViewModelFactory
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
                applicationContext,
                AppDatabase::class.java,
                "classes.db"
            ).fallbackToDestructiveMigration(true).build()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /* TODO(): REMOVE FUNCTION IN FINAL PRODUCT */
        lifecycleScope.launch {
            deleteAllClasses(db)
            deleteAllReminders(db)
            deleteAllAssignments(db)
            randomDataIntoClassDatabase(db)
            randomDataIntoReminderDatabase(db)
        }

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            val viewModelFactory = ViewModelFactory(db.schoolClassDao, db.reminderDao)
            val homeScreenViewModel: HomeScreenViewModel = viewModel(factory = viewModelFactory)

            Csc436schedulingappTheme {
                Scaffold(
                    topBar = {
                        TopAppBar(
                            title = {Text("CSC 436 Demo App", fontWeight = FontWeight.Bold)},
                            colors = TopAppBarDefaults.topAppBarColors(
                                containerColor = Color(94, 97, 242),
                                titleContentColor = Color.White
                            )
                        )
                    },
                    bottomBar = { BottomNavBar(navController) }
                ) { innerPadding ->
                    NavGraph(navController, innerPadding, homeScreenViewModel)
                }
            }
        }
    }

    /** TEMPORARY!! Just a bunch of functions to mess with for debugging **/
    companion object DebuggingFunctions {
        suspend fun randomDataIntoClassDatabase(db: AppDatabase): SchoolClass {
            val classesDao = db.schoolClassDao
            val datePattern = "yyyy-MM-dd HH:mm:ss"
            val dateFormat = SimpleDateFormat(datePattern, Locale.getDefault())

            val schoolClass = SchoolClass(
                name = "CSC 436",
                location = "Room 225, Frank E Pilling Building",
                startDate = dateFormat.parse("2025-09-15 00:00:00"),
                endDate = dateFormat.parse("2025-12-06 00:00:00"),
                startTime = dateFormat.parse("2020-01-01 08:00:00"),
                endTime = dateFormat.parse("2020-01-01 10:00:00"),
                days = DayList(listOf("Monday", "Wednesday", "Thursday", "Friday"))
            )
            classesDao.upsertClass(schoolClass)
            return schoolClass
        }

        suspend fun deleteAllClasses(db: AppDatabase) {
            val classesDao = db.schoolClassDao
            classesDao.deleteAllClasses()
        }

        suspend fun randomDataIntoReminderDatabase(db: AppDatabase): Reminder {
            val reminderDao = db.reminderDao
            val datePattern = "yyyy-MM-dd HH:mm:ss"
            val dateFormat = SimpleDateFormat(datePattern, Locale.getDefault())

            val reminder = Reminder(
                title = "Study for exam",
                location = "Library",
                date = dateFormat.parse("2025-12-04 00:00:00"),
                time = dateFormat.parse("2020-01-01 18:00:00")
            )
            reminderDao.upsertReminder(reminder)
            return reminder
        }

        suspend fun deleteAllReminders(db: AppDatabase) {
            val reminderDao = db.reminderDao
            reminderDao.deleteAllReminders()
        }

        suspend fun randomDataIntoAssignmentDatabase(db: AppDatabase): Assignment {
            val assignmentDao = db.assignmentDao
            val datePattern = "yyyy-MM-dd HH:mm:ss"
            val dateFormat = SimpleDateFormat(datePattern, Locale.getDefault())

            val assignment = Assignment(
                title = "Milestone 1",
                date = dateFormat.parse("2025-12-03 00:00:00"),
                time = dateFormat.parse("2020-01-01 18:00:00"),
                classId = 1
            )
            assignmentDao.upsertAssignment(assignment)
            return assignment
        }

        suspend fun deleteAllAssignments(db: AppDatabase) {
            val assignmentDao = db.assignmentDao
            assignmentDao.deleteAllAssignments()
        }
    }

}
