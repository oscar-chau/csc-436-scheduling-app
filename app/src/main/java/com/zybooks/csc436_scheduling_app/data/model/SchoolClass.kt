package com.zybooks.csc436_scheduling_app.data.model

import java.util.Date

data class SchoolClass(
    val id: Integer,
    val name: String,
    val location: String?,
    val startDate: Date,
    val endDate: Date,
    val time: Date,
    val days: Array<String>
)