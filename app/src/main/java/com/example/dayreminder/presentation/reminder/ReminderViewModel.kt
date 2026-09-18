package com.example.dayreminder.presentation.reminder

import androidx.lifecycle.ViewModel
import com.example.dayreminder.domain.usecase.AddReminder
import com.example.dayreminder.domain.usecase.DeleteReminder
import com.example.dayreminder.domain.usecase.GetAllReminders
import com.example.dayreminder.domain.usecase.UpdateReminder
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import androidx.lifecycle.viewModelScope
import com.example.dayreminder.domain.model.Reminder
import com.example.dayreminder.presentation.reminder.state.AddReminderUiState
import com.example.dayreminder.presentation.reminder.state.CalendarUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlinx.coroutines.flow.combine
import java.time.Instant
import java.time.ZoneId

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val addReminderUseCase: AddReminder,
    private val getAllReminders: GetAllReminders,
    private val updateReminder: UpdateReminder,
    private val deleteReminderUseCase: DeleteReminder
) : ViewModel() {
    private val _calendarUiState = MutableStateFlow(
        CalendarUiState()
    )

    val calendarUiState: StateFlow<CalendarUiState> =
        _calendarUiState

    val reminders: StateFlow<List<Reminder>> =
        getAllReminders()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    private val _addReminderUiState = MutableStateFlow(
        AddReminderUiState()
    )

    val addReminderUiState: StateFlow<AddReminderUiState> =
        _addReminderUiState

    fun onDeleteReminder(reminder: Reminder) {
        viewModelScope.launch {
            deleteReminderUseCase(reminder)
        }
    }



    fun resetAddReminderUiState() {
        _addReminderUiState.value = AddReminderUiState()
    }
    fun addReminder(
        title: String,
        dateTimeMillis: Long
    ) {
        viewModelScope.launch {

            val reminder = Reminder(
                title = title,
                dateTimeMillis = dateTimeMillis
            )

            val isSaved = addReminderUseCase(
                reminder = reminder,
                currentTime = System.currentTimeMillis()
            )

            if (isSaved) {
                _addReminderUiState.value = AddReminderUiState(
                    isSaved = true
                )
            } else {
                _addReminderUiState.value = AddReminderUiState(
                    errorMessage = "Reminder time must be in the future"
                )
            }
        }
    }
    fun onDateSelected(date: LocalDate) {
        _calendarUiState.update { currentState ->
            currentState.copy(
                selectedDate = date
            )
        }
    }
    fun onNextMonth() {
        _calendarUiState.update { currentState ->
            currentState.copy(
                currentMonth = currentState.currentMonth.plusMonths(1)
            )
        }
    }

    fun onPreviousMonth() {
        _calendarUiState.update { currentState ->
            currentState.copy(
                currentMonth = currentState.currentMonth.minusMonths(1)
            )
        }
    }
    val selectedDateReminders: StateFlow<List<Reminder>> =
        combine(
            reminders,
            calendarUiState
        ) { remindersList, calendarState ->

            remindersList.filter { reminder ->

                val reminderDate = Instant
                    .ofEpochMilli(reminder.dateTimeMillis)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDate()

                reminderDate == calendarState.selectedDate
            }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    fun onReminderCompletedChange(
        reminder: Reminder,
        isCompleted: Boolean
    ) {
        viewModelScope.launch {

            val updatedReminder = reminder.copy(
                isCompleted = isCompleted
            )

            updateReminder(updatedReminder)
        }
    }
}