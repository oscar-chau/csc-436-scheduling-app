package com.zybooks.csc436_scheduling_app.navigation

import android.graphics.Color.rgb
import androidx.compose.animation.animateColorAsState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import kotlinx.serialization.Serializable

enum class AppScreen(val route: @Serializable Any, val title: String, val icon: ImageVector) {
    HOME(Routes.Home, "Home", Icons.Default.Home),
    CALENDAR(Routes.Calendar, "Calendar", Icons.Default.DateRange)
}

@Composable
fun BottomNavBar(navController: NavController) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    NavigationBar(containerColor = Color.White) {
        AppScreen.entries.forEach { item ->
            val selected = currentRoute == item.route.javaClass.name
            val iconColor by animateColorAsState(
                targetValue = if (selected) Color(94, 97, 242)
                else Color.Gray

            )

            NavigationBarItem(
                selected = selected,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                    }
                },
                icon = {
                    Icon(item.icon, contentDescription = item.title, tint = iconColor)
                },
                label = {
                    Text(item.title, color = iconColor)
                }
            )
        }
    }
}