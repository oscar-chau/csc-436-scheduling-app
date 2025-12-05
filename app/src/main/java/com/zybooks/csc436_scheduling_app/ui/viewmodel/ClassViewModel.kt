package com.zybooks.csc436_scheduling_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zybooks.csc436_scheduling_app.data.events.SchoolClassEvent
import com.zybooks.csc436_scheduling_app.data.local.SchoolClassDao
import com.zybooks.csc436_scheduling_app.data.model.SchoolClass
import com.zybooks.csc436_scheduling_app.data.sort.ClassSortType
import com.zybooks.csc436_scheduling_app.data.state.ClassState
import androidx.lifecycle.viewModelScope
import com.zybooks.csc436_scheduling_app.data.model.DayList
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class ClassViewModel(
    private val dao: SchoolClassDao
): ViewModel() {
    private val _sortType = MutableStateFlow(ClassSortType.NAME)

    // TODO(): This is only temporary, debating if we should let users do sorting ?
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _classes = _sortType.flatMapLatest { sortType ->
        when (sortType) {
            ClassSortType.NAME -> dao.getClasses()
            else -> dao.getClasses()
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())
    private val _state = MutableStateFlow(ClassState())

    val state = combine(_state, _sortType, _classes) { state, sortType, classes ->
        state.copy(
            classes = classes,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), ClassState())

    fun onEvent(event: SchoolClassEvent) {
        when(event) {
            is SchoolClassEvent.showDialog -> {
                _state.update { it.copy(
                    isAddingClass = true
                ) }
            }
            is SchoolClassEvent.hideDialog -> {
                _state.update { it.copy(
                    isAddingClass = false
                ) }
            }
            is  SchoolClassEvent.saveClass -> {
                val name = state.value.name
                val location = state.value.location
                val startDate = state.value.startDate
                val endDate = state.value.endDate
                val startTime = state.value.startTime
                val endTime = state.value.endTime
                val days = state.value.days

                // TODO(): add logic for checking blank values

                val schoolClass = SchoolClass(
                    name, location, startDate, endDate, startTime, endTime, days
                )

                viewModelScope.launch {
                    dao.upsertClass(schoolClass)
                }

                _state.update { it.copy(
                    name = "",
                    location = null,
                    startDate = Date(0),
                    endDate = Date(0),
                    startTime = Date(0),
                    endTime = Date(0),
                    days = DayList(emptyList()),
                    isAddingClass = false
                )}
            }
            is SchoolClassEvent.setName -> {
                _state.update {
                    it.copy(
                        name = event.name
                    )
                }
            }
            is SchoolClassEvent.setLocation -> {
                _state.update {
                    it.copy(
                        location = event.location
                    )
                }
            }
            is SchoolClassEvent.setStartTime -> {
                _state.update {
                    it.copy(
                        startTime = event.startTime
                    )
                }
            }
            is SchoolClassEvent.setEndTime -> {
                _state.update {
                    it.copy(
                        endTime = event.endTime
                    )
                }
            }
            is SchoolClassEvent.setStartDate -> {
                _state.update {
                    it.copy(
                        startDate = event.startDate
                    )
                }
            }
            is SchoolClassEvent.setEndDate -> {
                _state.update {
                    it.copy(
                        endDate = event.endDate
                    )
                }
            }
            is SchoolClassEvent.setDays -> {
                _state.update {
                    it.copy(
                        days = event.days
                    )
                }
            }
            is SchoolClassEvent.deleteClass -> {
                viewModelScope.launch {
                    dao.deleteClass(event.schoolClass)
                }
            }
            else -> {
                println("TRIED TO USE ${event::class.simpleName}")
            }
        }
    }
}