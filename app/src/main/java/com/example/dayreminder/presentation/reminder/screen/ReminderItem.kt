package com.example.dayreminder.presentation.reminder.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.dayreminder.domain.model.Reminder
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.ui.Alignment

@Composable
fun ReminderItem(
    reminder: Reminder,
    onCompletedChange: (Boolean) -> Unit
) {

    val time = Instant
        .ofEpochMilli(reminder.dateTimeMillis)
        .atZone(ZoneId.systemDefault())
        .toLocalTime()
        .format(
            DateTimeFormatter.ofPattern("hh:mm a")
        )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                horizontal = 16.dp,
                vertical = 4.dp
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = reminder.title
                )

                Text(
                    text = time
                )
            }

            Checkbox(
                checked = reminder.isCompleted,
                onCheckedChange = onCompletedChange
            )
        }
    }
}