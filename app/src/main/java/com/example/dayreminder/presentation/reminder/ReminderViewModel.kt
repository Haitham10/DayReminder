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
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@HiltViewModel
class ReminderViewModel @Inject constructor(
    private val addReminderUseCase: AddReminder,
    private val getAllReminders: GetAllReminders,
    private val updateReminder: UpdateReminder,
    private val deleteReminder: DeleteReminder
) : ViewModel() {

    val reminders: StateFlow<List<Reminder>> =
        getAllReminders()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )
    fun addReminder(
        title: String,
        dateTimeMillis: Long
    ) {
        viewModelScope.launch {

            val reminder = Reminder(
                title = title,
                dateTimeMillis = dateTimeMillis
            )

            addReminderUseCase(
                reminder = reminder,
                currentTime = System.currentTimeMillis()
            )
        }
    }
}