package com.example.dayreminder.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.dayreminder.data.local.entity.ReminderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReminderDao {

    @Insert
    suspend fun insertReminder(
        reminder: ReminderEntity
    ): Long

    @Query("SELECT * FROM reminders ORDER BY dateTimeMillis ASC")
    fun getAllReminders(): Flow<List<ReminderEntity>>

    @Update
    suspend fun updateReminder(
        reminder: ReminderEntity
    )

    @Delete
    suspend fun deleteReminder(
        reminder: ReminderEntity
    )
}