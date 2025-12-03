package com.zybooks.csc436_scheduling_app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zybooks.csc436_scheduling_app.data.events.ReminderEvent
import com.zybooks.csc436_scheduling_app.data.local.ReminderDao
import com.zybooks.csc436_scheduling_app.data.model.Reminder
import com.zybooks.csc436_scheduling_app.data.state.ReminderState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date


class ReminderViewModel(private val dao: ReminderDao): ViewModel() {
    // way better than wtf was happening in class view model, dont sort
    private val _reminders: StateFlow<List<Reminder>> = dao.getAllReminders()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyList()
        )

    private val _state = MutableStateFlow(ReminderState())

    val reminderState: StateFlow<ReminderState> = combine(_state, _reminders) { state, reminders ->
        state.copy(
            reminders = reminders
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000L), ReminderState())

    fun onEvent(event: ReminderEvent) {
        when(event) {
            is ReminderEvent.saveReminder -> {
                val title = reminderState.value.title
                val location = reminderState.value.location
                val date = reminderState.value.date
                val time = reminderState.value.time

                // TODO(): Add logic for checking blank values

                val reminder = Reminder(title, location, date, time)

                viewModelScope.launch {
                    dao.upsertReminder(reminder)
                }

                _state.update { it.copy(
                    title = "",
                    location = null,
                    date = Date(0),
                    time = Date(0),
                    isAddingReminder = false
                )}
            }
            is ReminderEvent.showDialog -> {
                _state.update {it.copy(
                    isAddingReminder = true
                )}
            }
            is ReminderEvent.hideDialog -> {
                _state.update {it.copy(
                    isAddingReminder = false
                )}
            }
            is ReminderEvent.setTitle -> {
                _state.update {
                    it.copy(
                        title = event.title
                    )
                }

            }
            is ReminderEvent.setLocation -> {
                _state.update {
                    it.copy(
                        location = event.location
                    )
                }

            }
            is ReminderEvent.setDate -> {
                _state.update {
                    it.copy(
                        date = event.date
                    )
                }
            }
            is ReminderEvent.setTime -> {
                _state.update {
                    it.copy(
                        time = event.time
                    )
                }
            }
        }
    }
}