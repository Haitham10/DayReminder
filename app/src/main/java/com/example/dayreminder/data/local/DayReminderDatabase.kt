package com.example.dayreminder.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.dayreminder.data.local.dao.ReminderDao
import com.example.dayreminder.data.local.entity.ReminderEntity

@Database(
    entities = [ReminderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class DayReminderDatabase : RoomDatabase() {

    abstract fun reminderDao(): ReminderDao

}