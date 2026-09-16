package com.example.dayreminder.data.mapper

import com.example.dayreminder.data.local.entity.ReminderEntity
import com.example.dayreminder.domain.model.Reminder

fun ReminderEntity.toDomain(): Reminder {
    return Reminder(
        id = id,
        title = title,
        description = description,
        dateTimeMillis = dateTimeMillis,
        isCompleted = isCompleted
    )
}

fun Reminder.toEntity(): ReminderEntity {
    return ReminderEntity(
        id = id,
        title = title,
        description = description,
        dateTimeMillis = dateTimeMillis,
        isCompleted = isCompleted
    )
}