package com.example.dayreminder.domain.usecase

import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Test

class ValidateReminderDateTimeTest {
    @Test
    fun `reminder time in the past returns false`() {

        val validateReminderDateTime = ValidateReminderDateTime()

        val currentTime = 1_000L
        val reminderTime = 500L

        val result = validateReminderDateTime(
            reminderTime = reminderTime,
            currentTime = currentTime
        )

        assertFalse(result)
    }
    @Test
    fun `reminder time in the future returns true`() {

        val validateReminderDateTime = ValidateReminderDateTime()

        val currentTime = 1_000L
        val reminderTime = 1_500L

        val result = validateReminderDateTime(
            reminderTime = reminderTime,
            currentTime = currentTime
        )

        assertTrue(result)
    }
    @Test
    fun `reminder time equal to current time returns false`() {

        val validateReminderDateTime = ValidateReminderDateTime()

        val currentTime = 1_000L
        val reminderTime = 1_000L

        val result = validateReminderDateTime(
            reminderTime = reminderTime,
            currentTime = currentTime
        )

        assertFalse(result)
    }
}