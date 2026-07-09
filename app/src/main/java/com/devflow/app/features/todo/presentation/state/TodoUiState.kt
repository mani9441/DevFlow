package com.devflow.app.features.todo.presentation.state

import com.devflow.app.features.todo.domain.model.Todo

data class TodoUiState(

    val todos: List<Todo> = emptyList(),

    val selectedTodo: Todo? = null,

    val isLoading: Boolean = false,

    val error: String? = null
)
