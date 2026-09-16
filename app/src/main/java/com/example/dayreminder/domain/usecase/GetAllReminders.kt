package com.example.dayreminder.domain.usecase

import com.example.dayreminder.domain.model.Reminder
import com.example.dayreminder.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllReminders @Inject constructor(
    private val repository: ReminderRepository
) {

    operator fun invoke(): Flow<List<Reminder>> {
        return repository.getAllReminders()
    }
}