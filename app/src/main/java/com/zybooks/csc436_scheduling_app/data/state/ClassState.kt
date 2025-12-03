package com.zybooks.csc436_scheduling_app.data.state

import com.zybooks.csc436_scheduling_app.data.model.DayList
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import com.zybooks.csc436_scheduling_app.data.sort.ClassSortType
import java.util.Date

data class ClassState(
    val classes: List<SchoolClass> = emptyList(),
    val name: String = "",
    val location: String? = null,
    val startDate: Date = Date(0),
    val endDate: Date = Date(0),
    val startTime: Date = Date(0),
    val endTime: Date = Date(0),
    val days: DayList = DayList(emptyList()),
    val sortType: ClassSortType = ClassSortType.NAME,
    val isAddingClass: Boolean = false
)