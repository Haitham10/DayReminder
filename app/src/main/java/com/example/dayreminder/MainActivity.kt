package com.example.dayreminder

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.activity.compose.setContent
import com.example.dayreminder.presentation.reminder.ReminderViewModel
import com.example.dayreminder.presentation.reminder.screen.ReminderScreen
import com.example.dayreminder.ui.theme.DayReminderTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val reminderViewModel: ReminderViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            DayReminderTheme {
                ReminderScreen(
                    viewModel = reminderViewModel
                )
            }
        }
    }
}