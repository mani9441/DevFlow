package com.devflow.app.features.todo.domain.usecase

import com.devflow.app.features.todo.domain.model.Todo
import com.devflow.app.features.todo.domain.repository.TodoRepository
import javax.inject.Inject

class GetTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {

    suspend operator fun invoke(id: Long): Todo? {
        return repository.getTodo(id)
    }
}
