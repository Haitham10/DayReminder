package com.example.dayreminder.presentation.reminder.state

import java.time.LocalDate
import java.time.YearMonth

data class CalendarUiState(
    val currentMonth: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now()
) {
    val firstDayOffset: Int
        get() = currentMonth
            .atDay(1)
            .dayOfWeek
            .value - 1

    val daysInMonth: List<LocalDate>
        get() = (1..currentMonth.lengthOfMonth()).map { day ->
            currentMonth.atDay(day)
        }
}