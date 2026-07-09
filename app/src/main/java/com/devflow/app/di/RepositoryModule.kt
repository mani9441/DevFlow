package com.devflow.app.di

import com.devflow.app.features.deadlines.data.repository.DeadlineRepositoryImpl
import com.devflow.app.features.deadlines.domain.repository.DeadlineRepository
import com.devflow.app.features.meetings.data.repository.MeetingRepositoryImpl
import com.devflow.app.features.meetings.domain.repository.MeetingRepository
import com.devflow.app.features.notes.data.repository.NoteRepositoryImpl
import com.devflow.app.features.notes.domain.repository.NoteRepository
import com.devflow.app.features.todo.data.repository.TodoRepositoryImpl
import com.devflow.app.features.todo.domain.repository.TodoRepository
import com.devflow.app.features.sprint.data.repository.SprintRepositoryImpl
import com.devflow.app.features.sprint.domain.repository.SprintRepository
import com.devflow.app.features.tasks.data.repository.TaskRepositoryImpl
import com.devflow.app.features.tasks.domain.repository.TaskRepository
import com.devflow.app.features.stories.data.repository.UserStoryRepositoryImpl
import com.devflow.app.features.stories.domain.repository.UserStoryRepository
import com.devflow.app.features.progress.data.repository.SprintProgressRepositoryImpl
import com.devflow.app.features.progress.domain.repository.SprintProgressRepository
import com.devflow.app.features.communication.data.repository.TeamMemberRepositoryImpl
import com.devflow.app.features.communication.domain.repository.TeamMemberRepository
import com.devflow.app.features.communication.data.repository.MessageRepositoryImpl
import com.devflow.app.features.communication.domain.repository.MessageRepository
import com.devflow.app.features.issues.data.repository.IssueRepositoryImpl
import com.devflow.app.features.issues.domain.repository.IssueRepository
import com.devflow.app.features.monitoring.data.repository.DevelopmentMonitoringRepositoryImpl
import com.devflow.app.features.monitoring.domain.repository.DevelopmentMonitoringRepository
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
    abstract fun bindTodoRepository(
        repository: TodoRepositoryImpl
    ): TodoRepository

    @Binds
    @Singleton
    abstract fun bindNoteRepository(
        repository: NoteRepositoryImpl
    ): NoteRepository

    @Binds
    @Singleton
    abstract fun bindMeetingRepository(
        repository: MeetingRepositoryImpl
    ): MeetingRepository

    @Binds
    @Singleton
    abstract fun bindDeadlineRepository(
        repository: DeadlineRepositoryImpl
    ): DeadlineRepository

    @Binds
    @Singleton
    abstract fun bindSprintRepository(
        repository: SprintRepositoryImpl
    ): SprintRepository

    @Binds
    @Singleton
    abstract fun bindTaskRepository(
        repository: TaskRepositoryImpl
    ): TaskRepository

    @Binds
    @Singleton
    abstract fun bindUserStoryRepository(
        repository: UserStoryRepositoryImpl
    ): UserStoryRepository

    @Binds
    @Singleton
    abstract fun bindSprintProgressRepository(
        repository: SprintProgressRepositoryImpl
    ): SprintProgressRepository

    @Binds
    @Singleton
    abstract fun bindTeamMemberRepository(
        repository: TeamMemberRepositoryImpl
    ): TeamMemberRepository

    @Binds
    @Singleton
    abstract fun bindMessageRepository(
        repository: MessageRepositoryImpl
    ): MessageRepository

    @Binds
    @Singleton
    abstract fun bindIssueRepository(
        repository: IssueRepositoryImpl
    ): IssueRepository

    @Binds
    @Singleton
    abstract fun bindDevelopmentMonitoringRepository(
        repository: DevelopmentMonitoringRepositoryImpl
    ): DevelopmentMonitoringRepository
}