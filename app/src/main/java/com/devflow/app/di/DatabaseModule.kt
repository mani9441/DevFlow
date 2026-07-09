package com.devflow.app.di

import android.content.Context
import androidx.room.Room
import com.devflow.app.data.database.DevFlowDatabase
import com.devflow.app.features.deadlines.data.local.dao.DeadlineDao
import com.devflow.app.features.meetings.data.local.dao.MeetingDao
import com.devflow.app.features.notes.data.local.dao.NoteDao
import com.devflow.app.features.todo.data.local.dao.TodoDao
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
    ): DevFlowDatabase {

        return Room.databaseBuilder(
            context,
            DevFlowDatabase::class.java,
            "devflow.db"
        )
        .fallbackToDestructiveMigration()
        .build()
    }

    @Provides
    fun provideTodoDao(
        database: DevFlowDatabase
    ): TodoDao {
        return database.todoDao()
    }

    @Provides
    fun provideNoteDao(
        database: DevFlowDatabase
    ): NoteDao {
        return database.noteDao()
    }

    @Provides
    fun provideMeetingDao(
        database: DevFlowDatabase
    ): MeetingDao {
        return database.meetingDao()
    }

    @Provides
    fun provideDeadlineDao(
        database: DevFlowDatabase
    ): DeadlineDao {
        return database.deadlineDao()
    }
}