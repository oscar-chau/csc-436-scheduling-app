package com.zybooks.csc436_scheduling_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.zybooks.csc436_scheduling_app.navigation.BottomNavBar
import com.zybooks.csc436_scheduling_app.navigation.NavGraph
import com.zybooks.csc436_scheduling_app.ui.theme.Csc436schedulingappTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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
}