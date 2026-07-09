package com.devflow.app.features.todo.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.todo.domain.model.Todo
import com.devflow.app.features.todo.domain.usecase.TodoUseCases
import com.devflow.app.features.todo.presentation.state.TodoUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class TodoViewModel @Inject constructor(
    private val useCases: TodoUseCases
) : ViewModel() {

    private val _uiState = MutableStateFlow(TodoUiState())
    val uiState: StateFlow<TodoUiState> = _uiState.asStateFlow()

    init {
        loadTodos()
    }

    fun loadTodos() {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            useCases.getAllTodos().collect { todos ->
                _uiState.update {
                    it.copy(
                        todos = todos,
                        isLoading = false,
                        error = null
                    )
                }
            }
        }
    }

    fun loadTodo(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val todo = useCases.getTodo(id)
            _uiState.update {
                it.copy(
                    selectedTodo = todo,
                    isLoading = false,
                    error = if (todo == null) "Todo not found" else null
                )
            }
        }
    }

    fun selectTodo(todo: Todo) {
        _uiState.update {
            it.copy(selectedTodo = todo)
        }
    }

    fun addTodo(
        title: String,
        description: String?,
        dueDate: LocalDate?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                useCases.createTodo(title, description, dueDate)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to add todo") }
            }
        }
    }

    fun updateTodo(todo: Todo, onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                useCases.updateTodo(todo)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to update todo") }
            }
        }
    }

    fun deleteTodo(todo: Todo, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                useCases.deleteTodo(todo)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to delete todo") }
            }
        }
    }

    fun markCompleted(id: Long, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                useCases.markCompleted(id)
                // Reload the loaded todo if it was the selected one
                val currentSelected = _uiState.value.selectedTodo
                if (currentSelected != null && currentSelected.id == id) {
                    loadTodo(id)
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(error = e.message ?: "Failed to complete todo") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(error = null) }
    }
}
