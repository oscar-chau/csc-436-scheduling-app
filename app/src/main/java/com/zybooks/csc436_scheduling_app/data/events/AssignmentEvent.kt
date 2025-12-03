package com.zybooks.csc436_scheduling_app.data.events

import com.zybooks.csc436_scheduling_app.data.model.Assignment
import java.util.Date

sealed interface AssignmentEvent {
    object saveAssignment : AssignmentEvent
    object showDialog: AssignmentEvent
    object hideDialog: AssignmentEvent
    data class setName(val name: String) : AssignmentEvent
    data class setDate(val date: Date) : AssignmentEvent
    data class setTime(val time: Date) : AssignmentEvent
    data class setClassId(val classId: Int) : AssignmentEvent
    data class deleteAssignment(val assignment: Assignment) : AssignmentEvent
}