package com.zybooks.csc436_scheduling_app.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.zybooks.csc436_scheduling_app.ui.screens.CalendarScreen
import com.zybooks.csc436_scheduling_app.ui.screens.Home
import com.zybooks.csc436_scheduling_app.ui.viewmodel.CalendarViewModel
import com.zybooks.csc436_scheduling_app.ui.viewmodel.HomeScreenViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NavGraph(
    navController: NavHostController,
    innerPadding: PaddingValues,
    homeVm: HomeScreenViewModel,
    calendarVm: CalendarViewModel
) {
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
            Home(homeVm)
        }
        composable<Routes.Calendar> {
            CalendarScreen(calendarVm)
        }
    }
}