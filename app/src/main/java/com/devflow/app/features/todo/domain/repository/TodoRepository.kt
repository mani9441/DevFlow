package com.devflow.app.features.todo.domain.repository

import com.devflow.app.features.todo.domain.model.Todo
import kotlinx.coroutines.flow.Flow

interface TodoRepository {

    suspend fun createTodo(todo: Todo): Long

    suspend fun updateTodo(todo: Todo)

    suspend fun deleteTodo(todo: Todo)

    suspend fun markCompleted(id: Long)

    suspend fun getTodo(id: Long): Todo?

    fun getAllTodos(): Flow<List<Todo>>
}