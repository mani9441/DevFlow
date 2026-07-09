package com.devflow.app.features.todo.domain.usecase

import com.devflow.app.features.todo.domain.model.Todo
import com.devflow.app.features.todo.domain.repository.TodoRepository
import javax.inject.Inject

class DeleteTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {

    suspend operator fun invoke(todo: Todo) {
        repository.deleteTodo(todo)
    }
}
