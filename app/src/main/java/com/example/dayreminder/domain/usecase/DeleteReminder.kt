package com.example.dayreminder.domain.usecase

import com.example.dayreminder.domain.model.Reminder
import com.example.dayreminder.domain.repository.ReminderRepository
import javax.inject.Inject

class DeleteReminder @Inject constructor(
    private val repository: ReminderRepository
) {

    suspend operator fun invoke(reminder: Reminder) {
        repository.deleteReminder(reminder)
    }
}