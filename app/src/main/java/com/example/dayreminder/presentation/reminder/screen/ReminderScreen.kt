package com.example.dayreminder.presentation.reminder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.dayreminder.presentation.reminder.ReminderViewModel

@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel
) {

    val reminders by viewModel.reminders.collectAsState()

    Column {

        Text(
            text = "Reminders count: ${reminders.size}"
        )

        Button(
            onClick = {
                viewModel.addReminder(
                    title = "Test Reminder",
                    dateTimeMillis = System.currentTimeMillis() + 60_000
                )
            }
        ) {
            Text("Add Test Reminder")
        }
    }
}