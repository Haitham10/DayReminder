package com.example.dayreminder.domain.repository

import com.example.dayreminder.domain.model.Reminder
import kotlinx.coroutines.flow.Flow

interface ReminderRepository {

    suspend fun insertReminder(reminder: Reminder): Long

    fun getAllReminders(): Flow<List<Reminder>>

    suspend fun updateReminder(reminder: Reminder)

    suspend fun deleteReminder(reminder: Reminder)
}