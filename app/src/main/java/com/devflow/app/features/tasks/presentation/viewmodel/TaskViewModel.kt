package com.devflow.app.features.tasks.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.tasks.domain.model.Task
import com.devflow.app.features.tasks.domain.model.TaskPriority
import com.devflow.app.features.tasks.domain.model.TaskStatus
import com.devflow.app.features.tasks.domain.repository.TaskRepository
import com.devflow.app.features.tasks.presentation.state.TaskUiState
import com.devflow.app.features.sprint.domain.repository.SprintRepository
import com.devflow.app.features.project.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository,
    private val sprintRepository: SprintRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TaskUiState())
    val uiState: StateFlow<TaskUiState> = _uiState.asStateFlow()

    init {
        loadTasks()
        loadAvailableSprints()
    }

    fun loadTasks() {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                projectRepository.getActiveProject().collect { project ->
                    if (project != null) {
                        launch {
                            repository.getAllTasks(project.id).collect { tasks ->
                                _uiState.update { it.copy(taskList = tasks, loadingState = false, errorMessage = null) }
                            }
                        }
                        launch {
                            repository.getUnassignedTasks(project.id).collect { tasks ->
                                _uiState.update { it.copy(unassignedTasks = tasks) }
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                taskList = emptyList(),
                                unassignedTasks = emptyList(),
                                loadingState = false,
                                errorMessage = "No active project selected"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingState = false,
                        errorMessage = e.message ?: "Failed to load tasks"
                    )
                }
            }
        }
    }

    fun loadTask(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingState = true) }
            val task = repository.getTask(id)
            _uiState.update {
                it.copy(
                    selectedTask = task,
                    loadingState = false,
                    errorMessage = if (task == null) "Task not found" else null
                )
            }
        }
    }

    private fun loadAvailableSprints() {
        viewModelScope.launch {
            try {
                projectRepository.getActiveProject().collect { project ->
                    if (project != null) {
                        sprintRepository.getAllSprints(project.id).collect { sprints ->
                            _uiState.update { it.copy(availableSprints = sprints) }
                        }
                    } else {
                        _uiState.update { it.copy(availableSprints = emptyList()) }
                    }
                }
            } catch (e: Exception) {
                // Ignore or handle
            }
        }
    }

    fun addTask(
        title: String,
        description: String?,
        priority: TaskPriority,
        dueDate: LocalDate?,
        sprintId: Long?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val project = projectRepository.getActiveProject().first()
                if (project != null) {
                    val newTask = Task(
                        projectId = project.id,
                        title = title.trim(),
                        description = description?.trim(),
                        priority = priority,
                        status = TaskStatus.PENDING,
                        dueDate = dueDate,
                        sprintId = sprintId,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                    repository.createTask(newTask)
                    onSuccess()
                } else {
                    _uiState.update { it.copy(errorMessage = "No active project selected") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to add task") }
            }
        }
    }

    fun editTask(
        task: Task,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.updateTask(task)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update task") }
            }
        }
    }

    fun updateStatus(
        task: Task,
        status: TaskStatus,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val updated = task.copy(status = status)
                repository.updateTask(updated)
                if (_uiState.value.selectedTask?.id == task.id) {
                    _uiState.update { it.copy(selectedTask = updated) }
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update status") }
            }
        }
    }

    fun assignSprint(
        task: Task,
        sprintId: Long?,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val updated = task.copy(sprintId = sprintId)
                repository.updateTask(updated)
                if (_uiState.value.selectedTask?.id == task.id) {
                    _uiState.update { it.copy(selectedTask = updated) }
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to assign sprint") }
            }
        }
    }

    fun deleteTask(
        task: Task,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteTask(task)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to delete task") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
