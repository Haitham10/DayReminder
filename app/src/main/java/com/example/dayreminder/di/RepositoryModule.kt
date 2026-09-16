package com.example.dayreminder.di

import com.example.dayreminder.data.repository.ReminderRepositoryImpl
import com.example.dayreminder.domain.repository.ReminderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindReminderRepository(
        implementation: ReminderRepositoryImpl
    ): ReminderRepository

}