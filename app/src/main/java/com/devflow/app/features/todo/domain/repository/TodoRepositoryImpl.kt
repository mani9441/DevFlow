package com.devflow.app.features.todo.data.repository

import com.devflow.app.features.todo.data.local.dao.TodoDao
import com.devflow.app.features.todo.data.mapper.toDomain
import com.devflow.app.features.todo.data.mapper.toEntity
import com.devflow.app.features.todo.domain.model.Todo
import com.devflow.app.features.todo.domain.model.TodoStatus
import com.devflow.app.features.todo.domain.repository.TodoRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class TodoRepositoryImpl @Inject constructor(
    private val dao: TodoDao
) : TodoRepository {

    override suspend fun createTodo(todo: Todo): Long {
        return dao.insert(todo.toEntity())
    }

    override suspend fun updateTodo(todo: Todo) {

        val updatedTodo = todo.copy(
            updatedAt = LocalDateTime.now()
        )

        dao.update(updatedTodo.toEntity())
    }

    override suspend fun deleteTodo(todo: Todo) {
        dao.delete(todo.toEntity())
    }

    override suspend fun markCompleted(id: Long) {

        dao.updateStatus(
            id = id,
            status = TodoStatus.COMPLETED.name,
            updatedAt = LocalDateTime.now()
        )
    }

    override suspend fun getTodo(id: Long): Todo? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAllTodos(): Flow<List<Todo>> {
        return dao
            .getAll()
            .map { list ->
                list.map { it.toDomain() }
            }
    }
}