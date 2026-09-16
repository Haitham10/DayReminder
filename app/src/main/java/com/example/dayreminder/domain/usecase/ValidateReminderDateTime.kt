package com.example.dayreminder.domain.usecase

import javax.inject.Inject

class ValidateReminderDateTime @Inject constructor() {

    operator fun invoke(
        reminderTime: Long,
        currentTime: Long
    ): Boolean {
        return reminderTime > currentTime
    }
}