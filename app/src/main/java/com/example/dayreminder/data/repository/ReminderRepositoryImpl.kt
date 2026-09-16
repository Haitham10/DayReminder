package com.example.dayreminder.data.repository

import com.example.dayreminder.data.local.dao.ReminderDao
import com.example.dayreminder.data.mapper.toDomain
import com.example.dayreminder.data.mapper.toEntity
import com.example.dayreminder.domain.model.Reminder
import com.example.dayreminder.domain.repository.ReminderRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ReminderRepositoryImpl @Inject constructor(
    private val reminderDao: ReminderDao
) : ReminderRepository {

    override suspend fun insertReminder(reminder: Reminder): Long {
        return reminderDao.insertReminder(
            reminder.toEntity()
        )
    }
    override fun getAllReminders(): Flow<List<Reminder>> {
        return reminderDao.getAllReminders()
            .map { entities ->
                entities.map { entity ->
                    entity.toDomain()
                }
            }
    }

    override suspend fun updateReminder(reminder: Reminder) {
        reminderDao.updateReminder(
            reminder.toEntity()
        )
    }

    override suspend fun deleteReminder(reminder: Reminder) {
        reminderDao.deleteReminder(
            reminder.toEntity()
        )
    }

}