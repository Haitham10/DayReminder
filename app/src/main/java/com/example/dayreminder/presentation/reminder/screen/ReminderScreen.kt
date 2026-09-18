package com.example.dayreminder.presentation.reminder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.dayreminder.presentation.reminder.ReminderViewModel
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.IconButton
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.dayreminder.domain.model.Reminder
import java.time.Instant
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel
) {
    val calendarUiState by viewModel.calendarUiState.collectAsState()

    val monthTitle = calendarUiState.currentMonth.format(
        DateTimeFormatter.ofPattern("MMMM yyyy")
    )

    val selectedDateReminders by viewModel.selectedDateReminders.collectAsState()
    var showAddReminderDialog by remember {
        mutableStateOf(false)
    }
    val addReminderUiState by viewModel.addReminderUiState.collectAsState()

    var reminderToDelete by remember {
        mutableStateOf<Reminder?>(null)
    }

    LaunchedEffect(addReminderUiState.isSaved) {
        if (addReminderUiState.isSaved) {
            showAddReminderDialog = false
            viewModel.resetAddReminderUiState()
        }
    }

    Column {

        CalendarHeader(
            monthTitle = monthTitle,
            onPreviousMonth = viewModel::onPreviousMonth,
            onNextMonth = viewModel::onNextMonth
        )
        WeekDaysHeader()
        CalendarGrid(
            daysInMonth = calendarUiState.daysInMonth,
            firstDayOffset = calendarUiState.firstDayOffset,
            selectedDate = calendarUiState.selectedDate,
            onDateSelected = viewModel::onDateSelected
        )
        Button(
            onClick = {
                showAddReminderDialog = true
            }
        ) {
            Text("Add Reminder")
        }
        if (showAddReminderDialog) {

            var title by remember {
                mutableStateOf("")
            }
            var selectedTime by remember {
                mutableStateOf<LocalTime?>(null)
            }

            var showTimePicker by remember {
                mutableStateOf(false)
            }

            AlertDialog(
                onDismissRequest = {
                    showAddReminderDialog = false
                },

                title = {
                    Text("Add Reminder")
                },

                text = {
                    Column {

                        OutlinedTextField(
                            value = title,
                            onValueChange = { newTitle ->
                                title = newTitle
                            },
                            label = {
                                Text("Title")
                            }
                        )

                        Button(
                            onClick = {
                                showTimePicker = true
                            }
                        ) {
                            Text(
                                text = selectedTime?.toString() ?: "Select Time"
                            )
                        }

                        addReminderUiState.errorMessage?.let { errorMessage ->
                            Text(
                                text = errorMessage,
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                },

                confirmButton = {

                    Button(
                        enabled = title.isNotBlank() && selectedTime != null,
                        onClick = {

                            val time = selectedTime ?: return@Button

                            val dateTimeMillis = LocalDateTime
                                .of(
                                    calendarUiState.selectedDate,
                                    time
                                )
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()

                            viewModel.addReminder(
                                title = title.trim(),
                                dateTimeMillis = dateTimeMillis
                            )


                        }
                    ) {
                        Text("Save")
                    }
                },

                dismissButton = {
                    Button(
                        onClick = {
                            val time = selectedTime ?: return@Button

                            val dateTimeMillis = LocalDateTime
                                .of(
                                    calendarUiState.selectedDate,
                                    time
                                )
                                .atZone(ZoneId.systemDefault())
                                .toInstant()
                                .toEpochMilli()

                            viewModel.addReminder(
                                title = title.trim(),
                                dateTimeMillis = dateTimeMillis
                            )
                        }
                    ) {
                        Text("Cancel")
                    }
                }

            )
            if (showTimePicker) {

                val initialTime = selectedTime ?: LocalTime.now()

                val timePickerState = rememberTimePickerState(
                    initialHour = initialTime.hour,
                    initialMinute = initialTime.minute,
                    is24Hour = false
                )

                AlertDialog(
                    onDismissRequest = {
                        showAddReminderDialog = false
                        viewModel.resetAddReminderUiState()
                    },

                    text = {
                        TimePicker(
                            state = timePickerState
                        )
                    },

                    confirmButton = {
                        TextButton(
                            onClick = {
                                selectedTime = LocalTime.of(
                                    timePickerState.hour,
                                    timePickerState.minute
                                )

                                showTimePicker = false
                            }
                        ) {
                            Text("OK")
                        }
                    },

                    dismissButton = {
                        TextButton(
                            onClick = {
                                showTimePicker = false
                            }
                        ) {
                            Text("Cancel")
                        }
                    }
                )
            }
        }

        selectedDateReminders.forEach { reminder ->
            ReminderItem(
                reminder = reminder,

                onCompletedChange = { isCompleted ->
                    viewModel.onReminderCompletedChange(
                        reminder = reminder,
                        isCompleted = isCompleted
                    )
                },

                onDeleteClick = {
                    reminderToDelete = reminder
                }
            )
        }
    }
    reminderToDelete?.let { reminder ->

        AlertDialog(
            onDismissRequest = {
                reminderToDelete = null
            },

            title = {
                Text("Delete Reminder")
            },

            text = {
                Text(
                    "Are you sure you want to delete \"${reminder.title}\"?"
                )
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.onDeleteReminder(reminder)
                        reminderToDelete = null
                    }
                ) {
                    Text("Delete")
                }
            },

            dismissButton = {
                TextButton(
                    onClick = {
                        reminderToDelete = null
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}


@Composable
fun CalendarHeader(
    monthTitle: String,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth().padding(32.dp)
    ) {

        IconButton(
            onClick = onPreviousMonth
        ) {
            Text("<")
        }

        Text(
            text = monthTitle
        )

        IconButton(
            onClick = onNextMonth
        ) {
            Text(">")
        }
    }

}


@Composable
fun WeekDaysHeader() {

    val weekDays = listOf(
        "Mon",
        "Tue",
        "Wed",
        "Thu",
        "Fri",
        "Sat",
        "Sun"
    )

    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        weekDays.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
fun CalendarGrid(
    daysInMonth: List<LocalDate>,
    firstDayOffset: Int,
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {

    val calendarCells: List<LocalDate?> =
        List(firstDayOffset) { null } + daysInMonth

    Column {
        calendarCells
            .chunked(7)
            .forEach { week ->

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {

                    week.forEach { date ->

                        if (date != null) {

                            val isSelected = date == selectedDate

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clickable {
                                        onDateSelected(date)
                                    }
                                    .then(
                                        if (isSelected) {
                                            Modifier.background(
                                                color = MaterialTheme.colorScheme.primaryContainer,
                                                shape = CircleShape
                                            )
                                        } else {
                                            Modifier
                                        }
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = date.dayOfMonth.toString()
                                )
                            }

                        } else {

                            Spacer(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                            )
                        }
                    }

                    repeat(7 - week.size) {
                        Spacer(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(1f)
                        )
                    }
                }
            }
    }
}