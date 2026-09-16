package com.example.dayreminder.domain.usecase

import com.example.dayreminder.domain.model.Reminder
import com.example.dayreminder.domain.repository.ReminderRepository
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AddReminderTest {

    @Test
    fun `past reminder is not inserted`() = runTest {

        val repository = FakeReminderRepository()

        val addReminder = AddReminder(
            repository = repository,
            validateReminderDateTime = ValidateReminderDateTime()
        )

        val reminder = Reminder(
            title = "Study Kotlin",
            dateTimeMillis = 500L
        )

        val result = addReminder(
            reminder = reminder,
            currentTime = 1_000L
        )

        assertFalse(result)
        assertFalse(repository.insertWasCalled)
    }
    @Test
    fun `future reminder is inserted`() = runTest {

        val repository = FakeReminderRepository()

        val addReminder = AddReminder(
            repository = repository,
            validateReminderDateTime = ValidateReminderDateTime()
        )

        val reminder = Reminder(
            title = "Study Kotlin",
            dateTimeMillis = 1_500L
        )

        val result = addReminder(
            reminder = reminder,
            currentTime = 1_000L
        )

        assertTrue(result)
        assertTrue(repository.insertWasCalled)

        assertEquals(reminder, repository.insertedReminder)
    }
}

private class FakeReminderRepository : ReminderRepository {
    var insertedReminder: Reminder? = null

    var insertWasCalled = false

    override suspend fun insertReminder(reminder: Reminder): Long {
        insertWasCalled = true
        insertedReminder = reminder
        return 1L
    }

    override fun getAllReminders(): Flow<List<Reminder>> {
        return flowOf(emptyList())
    }

    override suspend fun updateReminder(reminder: Reminder) {
    }

    override suspend fun deleteReminder(reminder: Reminder) {
    }


}