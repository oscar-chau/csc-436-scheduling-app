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
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.zybooks.csc436_scheduling_app.data.local.ClassDatabase
import com.zybooks.csc436_scheduling_app.data.model.DayList
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import com.zybooks.csc436_scheduling_app.navigation.BottomNavBar
import com.zybooks.csc436_scheduling_app.navigation.NavGraph
import com.zybooks.csc436_scheduling_app.ui.theme.Csc436schedulingappTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val db by lazy {
        Room.databaseBuilder(
            applicationContext,
            ClassDatabase::class.java,
            "classes.db"
        ).build()
    }

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        /* TODO(): REMOVE FUNCTION IN FINAL PRODUCT */
        lifecycleScope.launch {
            deleteAllClasses(db)
        }
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

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
                    NavGraph(navController, innerPadding)
                }
            }
        }
    }

    /** TEMPORARY!! Just a bunch of functions to mess with for debugging **/
    companion object DebuggingFunctions {
        suspend fun randomDataIntoClassDatabase(db: ClassDatabase) {
            val classesDao = db.dao
            val datePattern = "yyyy-MM-dd HH:mm:ss"
            val dateFormat = SimpleDateFormat(datePattern, Locale.getDefault())

            val schoolClass = SchoolClass(
                name = "CSC 436",
                location = "Room 225, Frank E Pilling Building",
                startDate = dateFormat.parse("2025-09-15 00:00:00"),
                endDate = dateFormat.parse("2025-12-06 00:00:00"),
                startTime = dateFormat.parse("2020-01-01 08:00:00"),
                endTime = dateFormat.parse("2020-01-01 10:00:00"),
                days = DayList(listOf("Monday", "Wednesday", "Friday"))
            )
            classesDao.upsertClass(schoolClass)
        }

        suspend fun deleteAllClasses(db: ClassDatabase) {
            val classesDao = db.dao
            classesDao.deleteAllClasses()
        }
    }

}
