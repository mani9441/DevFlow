package com.devflow.app.di

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.room.RoomDatabase
import com.devflow.app.data.database.DevFlowDatabase
import com.devflow.app.features.deadlines.data.local.dao.DeadlineDao
import com.devflow.app.features.meetings.data.local.dao.MeetingDao
import com.devflow.app.features.notes.data.local.dao.NoteDao
import com.devflow.app.features.sprint.data.local.dao.SprintDao
import com.devflow.app.features.tasks.data.local.dao.TaskDao
import com.devflow.app.features.stories.data.local.dao.UserStoryDao
import com.devflow.app.features.todo.data.local.dao.TodoDao
import com.devflow.app.features.communication.data.local.dao.MessageDao
import com.devflow.app.features.communication.data.local.dao.TeamMemberDao
import com.devflow.app.features.issues.data.local.dao.IssueDao
import com.devflow.app.features.monitoring.data.local.dao.RepositoryConfigDao
import com.devflow.app.features.project.data.local.dao.ProjectDao
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
    fun provideProjectDao(
        database: DevFlowDatabase
    ): ProjectDao {
        return database.projectDao()
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

    @Provides
    fun provideSprintDao(
        database: DevFlowDatabase
    ): SprintDao {
        return database.sprintDao()
    }

    @Provides
    fun provideTaskDao(
        database: DevFlowDatabase
    ): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideUserStoryDao(
        database: DevFlowDatabase
    ): UserStoryDao {
        return database.userStoryDao()
    }

    @Provides
    fun provideTeamMemberDao(
        database: DevFlowDatabase
    ): TeamMemberDao {
        return database.teamMemberDao()
    }

    @Provides
    fun provideMessageDao(
        database: DevFlowDatabase
    ): MessageDao {
        return database.messageDao()
    }

    @Provides
    fun provideIssueDao(
        database: DevFlowDatabase
    ): IssueDao {
        return database.issueDao()
    }

    @Provides
    fun provideRepositoryConfigDao(
        database: DevFlowDatabase
    ): RepositoryConfigDao {
        return database.repositoryConfigDao()
    }
}