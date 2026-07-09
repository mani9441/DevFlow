package com.devflow.app.di

import com.devflow.app.features.todo.domain.repository.TodoRepository
import com.devflow.app.features.todo.domain.usecase.CreateTodoUseCase
import com.devflow.app.features.todo.domain.usecase.DeleteTodoUseCase
import com.devflow.app.features.todo.domain.usecase.GetAllTodosUseCase
import com.devflow.app.features.todo.domain.usecase.GetTodoUseCase
import com.devflow.app.features.todo.domain.usecase.MarkTodoCompletedUseCase
import com.devflow.app.features.todo.domain.usecase.TodoUseCases
import com.devflow.app.features.todo.domain.usecase.UpdateTodoUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideTodoUseCases(
        repository: TodoRepository
    ): TodoUseCases {

        return TodoUseCases(
            createTodo = CreateTodoUseCase(repository),
            updateTodo = UpdateTodoUseCase(repository),
            deleteTodo = DeleteTodoUseCase(repository),
            markCompleted = MarkTodoCompletedUseCase(repository),
            getTodo = GetTodoUseCase(repository),
            getAllTodos = GetAllTodosUseCase(repository)
        )
    }
}
