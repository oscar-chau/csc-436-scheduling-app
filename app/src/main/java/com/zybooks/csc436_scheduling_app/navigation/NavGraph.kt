package com.zybooks.csc436_scheduling_app.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zybooks.csc436_scheduling_app.ui.screens.Calendar
import com.zybooks.csc436_scheduling_app.ui.screens.Home

@Composable
fun NavGraph(navController: NavHostController, innerPadding: PaddingValues) {
    NavHost(
        navController = navController,
        startDestination = Routes.Home,
        modifier = Modifier.padding(innerPadding),
        enterTransition = { EnterTransition.None },
        exitTransition = { ExitTransition.None },
        popEnterTransition = { EnterTransition.None },
        popExitTransition = { ExitTransition.None }
    ) {
        composable<Routes.Home> {
            Home()
        }
        composable<Routes.Calendar> {
            Calendar()
        }
    }
}
