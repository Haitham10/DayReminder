package com.example.dayreminder.domain.model

data class Reminder( val id: Long = 0,
                     val title: String,
                     val description: String = "",
                     val dateTimeMillis: Long,
                     val isCompleted: Boolean = false)
