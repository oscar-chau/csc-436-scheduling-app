package com.zybooks.csc436_scheduling_app.data.events

import com.zybooks.csc436_scheduling_app.data.model.DayList
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import com.zybooks.csc436_scheduling_app.data.sort.ClassSortType
import java.util.Date

sealed interface SchoolClassEvent {
    object saveClass: SchoolClassEvent
    data class setName(val name: String): SchoolClassEvent
    data class setLocation(val location: String): SchoolClassEvent
    data class setStartDate(val startDate: Date): SchoolClassEvent
    data class setEndDate(val endDate: Date): SchoolClassEvent
    data class setStartTime(val startTime: Date): SchoolClassEvent
    data class setEndTime(val endTime: Date): SchoolClassEvent

    data class setDays(val days: DayList): SchoolClassEvent
    object showDialog: SchoolClassEvent
    object hideDialog: SchoolClassEvent
    data class sortClasses(val sortType: ClassSortType): SchoolClassEvent
    data class deleteClass(val schoolClass: SchoolClass): SchoolClassEvent
}
