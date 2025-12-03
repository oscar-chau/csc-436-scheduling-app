package com.zybooks.csc436_scheduling_app.data.model

import java.util.Date

data class Reminder(
    val id: Integer,
    val title: String,
    val location: String?,
    val date: Date,
    val time: Date
)