package com.example.dayreminder.domain.usecase

import com.example.dayreminder.domain.model.Reminder
import com.example.dayreminder.domain.repository.ReminderRepository
import javax.inject.Inject

class AddReminder @Inject constructor(
    private val repository: ReminderRepository,
    private val validateReminderDateTime: ValidateReminderDateTime
) {

    suspend operator fun invoke(
        reminder: Reminder,
        currentTime: Long
    ): Boolean {

        val isValid = validateReminderDateTime(
            reminderTime = reminder.dateTimeMillis,
            currentTime = currentTime
        )

        if (!isValid) {
            return false
        }

        repository.insertReminder(reminder)

        return true
    }
}