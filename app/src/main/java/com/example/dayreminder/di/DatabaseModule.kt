
package com.example.dayreminder.di

import android.content.Context
import androidx.room.Room
import com.example.dayreminder.data.local.DayReminderDatabase
import com.example.dayreminder.data.local.dao.ReminderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): DayReminderDatabase {
        return Room.databaseBuilder(
            context,
            DayReminderDatabase::class.java,
            "day_reminder_database"
        ).build()
    }

    @Provides
    fun provideReminderDao(
        database: DayReminderDatabase
    ): ReminderDao {
        return database.reminderDao()
    }
}