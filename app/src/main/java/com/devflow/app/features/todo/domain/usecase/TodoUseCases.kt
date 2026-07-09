package com.devflow.app.features.todo.domain.usecase

data class TodoUseCases(

    val createTodo: CreateTodoUseCase,

    val updateTodo: UpdateTodoUseCase,

    val deleteTodo: DeleteTodoUseCase,

    val markCompleted: MarkTodoCompletedUseCase,

    val getTodo: GetTodoUseCase,

    val getAllTodos: GetAllTodosUseCase
)
