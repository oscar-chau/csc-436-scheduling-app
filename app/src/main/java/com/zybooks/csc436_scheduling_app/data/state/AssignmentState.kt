package com.zybooks.csc436_scheduling_app.data.state

import com.zybooks.csc436_scheduling_app.data.model.Assignment
import java.util.Date

data class AssignmentState(
    val assignments: List<Assignment> = emptyList(),
    val title: String = "",
    val date: Date = Date(0),
    val time: Date = Date(0),
    val classId: Int = 0,
    val isAddingAssignment: Boolean = false
    )