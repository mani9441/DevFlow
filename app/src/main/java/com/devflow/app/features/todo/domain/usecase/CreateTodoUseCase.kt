package com.devflow.app.features.todo.domain.usecase

import com.devflow.app.features.todo.domain.model.Todo
import com.devflow.app.features.todo.domain.repository.TodoRepository
import java.time.LocalDateTime
import javax.inject.Inject

class CreateTodoUseCase @Inject constructor(
    private val repository: TodoRepository
) {

    suspend operator fun invoke(
        title: String,
        description: String?,
        dueDate: java.time.LocalDate?
    ): Long {

        require(title.isNotBlank()) {
            "Title cannot be empty."
        }

        val now = LocalDateTime.now()

        val todo = Todo(
            title = title.trim(),
            description = description?.trim(),
            dueDate = dueDate,
            createdAt = now,
            updatedAt = now
        )

        return repository.createTodo(todo)
    }
}
