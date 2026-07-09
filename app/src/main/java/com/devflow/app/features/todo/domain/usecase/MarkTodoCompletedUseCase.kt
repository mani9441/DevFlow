package com.devflow.app.features.todo.domain.usecase

import com.devflow.app.features.todo.domain.repository.TodoRepository
import javax.inject.Inject

class MarkTodoCompletedUseCase @Inject constructor(
    private val repository: TodoRepository
) {

    suspend operator fun invoke(id: Long) {
        repository.markCompleted(id)
    }
}
