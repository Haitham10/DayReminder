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
import androidx.compose.material3.MaterialTheme
import java.time.Instant
import java.time.ZoneId


@Composable
fun ReminderScreen(
    viewModel: ReminderViewModel
) {
    val calendarUiState by viewModel.calendarUiState.collectAsState()

    val monthTitle = calendarUiState.currentMonth.format(
        DateTimeFormatter.ofPattern("MMMM yyyy")
    )

    val selectedDateReminders by viewModel.selectedDateReminders.collectAsState()

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

        selectedDateReminders.forEach { reminder ->
            ReminderItem(
                reminder = reminder,
                onCompletedChange = { isCompleted ->
                    viewModel.onReminderCompletedChange(
                        reminder = reminder,
                        isCompleted = isCompleted
                    )
                }
            )
        }
    }
}


// ضيف دي تحت ReminderScreen
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