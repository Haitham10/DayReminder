package com.example.dayreminder.presentation.reminder.state

data class AddReminderUiState(
    val isSaved: Boolean = false,
    val errorMessage: String? = null
)