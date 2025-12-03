package com.zybooks.csc436_scheduling_app.data.model

import java.util.Date

data class Assignment(
    val id: Integer,
    val classId: Integer?,
    val name: String,
    val dueDate: Date,
    val time: Date
)