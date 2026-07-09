package com.devflow.app.di

import com.devflow.app.features.deadlines.data.repository.DeadlineRepositoryImpl
import com.devflow.app.features.deadlines.domain.repository.DeadlineRepository
import com.devflow.app.features.meetings.data.repository.MeetingRepositoryImpl
import com.devflow.app.features.meetings.domain.repository.MeetingRepository
import com.devflow.app.features.notes.data.repository.NoteRepositoryImpl
import com.devflow.app.features.notes.domain.repository.NoteRepository
import com.devflow.app.features.todo.data.repository.TodoRepositoryImpl
import com.devflow.app.features.todo.domain.repository.TodoRepository
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
}