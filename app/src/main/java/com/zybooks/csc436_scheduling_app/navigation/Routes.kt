package com.zybooks.csc436_scheduling_app.navigation

import kotlinx.serialization.Serializable

sealed class Routes {
    @Serializable
    data object Home

    @Serializable
    data object Calendar
}