package com.zybooks.csc436_scheduling_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zybooks.csc436_scheduling_app.data.events.AssignmentEvent
import com.zybooks.csc436_scheduling_app.data.local.AssignmentDao
import com.zybooks.csc436_scheduling_app.data.model.Assignment
import com.zybooks.csc436_scheduling_app.data.state.AssignmentState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class AssignmentViewModel(
    private val dao: AssignmentDao
): ViewModel() {
    val _assignments = dao.getAllAssignments().stateIn(
        scope = viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        emptyList()
    )

    private val _state = MutableStateFlow(AssignmentState())

    val assignmentState: StateFlow<AssignmentState> = combine(_state, _assignments) {state, assignments ->
        state.copy (
            assignments = assignments
            )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), AssignmentState())

    fun onEvent(event: AssignmentEvent) {
        when(event) {
            is AssignmentEvent.saveAssignment -> {
                val title = assignmentState.value.title
                val date = assignmentState.value.date
                val time = assignmentState.value.time
                val classId = assignmentState.value.classId

                if (classId == 0) {
                    println("No class selected for assignment")
                    return
                }
                if (title.isBlank() || date.time == 0L || time.time == 0L) {
                    println("Forgot something for assignment: $title, $date, $time")
                }

                val assignment = Assignment(title, date, time, classId)

                viewModelScope.launch {
                    dao.upsertAssignment(assignment)
                }

                _state.update { it.copy(
                    title = "",
                    date = Date(0),
                    time = Date(0),
                    classId = 0,
                    isAddingAssignment = false
                )
                }
            }
            is AssignmentEvent.deleteAssignment -> {
                viewModelScope.launch {
                    dao.deleteAssignment(event.assignment)
                }
            }
            is AssignmentEvent.hideDialog -> {
                _state.update { it.copy(
                    isAddingAssignment = false
                ) }
            }
            is AssignmentEvent.showDialog -> {
                _state.update { it.copy(
                    isAddingAssignment = true
                ) }
            }
            is AssignmentEvent.setClassId -> {
                _state.update { it.copy(
                    classId = event.classId
                ) }
            }
            is AssignmentEvent.setDate -> {
                _state.update { it.copy(
                    date = event.date
                ) }
            }
            is AssignmentEvent.setName -> {
                _state.update { it.copy(
                    title = event.name
                ) }
            }
            is AssignmentEvent.setTime -> {
                _state.update { it.copy(
                    time = event.time
                ) }
            }
        }
    }


}