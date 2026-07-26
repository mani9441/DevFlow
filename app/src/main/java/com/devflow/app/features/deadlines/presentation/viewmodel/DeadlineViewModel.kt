package com.devflow.app.features.deadlines.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.deadlines.domain.model.Deadline
import com.devflow.app.features.deadlines.domain.repository.DeadlineRepository
import com.devflow.app.features.deadlines.presentation.state.DeadlineUiState
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
class DeadlineViewModel @Inject constructor(
    private val repository: DeadlineRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DeadlineUiState())
    val uiState: StateFlow<DeadlineUiState> = _uiState.asStateFlow()

    init {
        loadDeadlines()
    }

    fun loadDeadlines() {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                projectRepository.getActiveProject().collect { project ->
                    if (project != null) {
                        repository.getAllDeadlines(project.id).collect { deadlines ->
                            _uiState.update {
                                it.copy(
                                    deadlineList = deadlines,
                                    loadingState = false,
                                    errorMessage = null
                                )
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                deadlineList = emptyList(),
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
                        errorMessage = e.message ?: "Failed to load deadlines"
                    )
                }
            }
        }
    }

    fun loadDeadline(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingState = true) }
            val deadline = repository.getDeadline(id)
            _uiState.update {
                it.copy(
                    selectedDeadline = deadline,
                    loadingState = false,
                    errorMessage = if (deadline == null) "Deadline not found" else null
                )
            }
        }
    }

    fun selectDeadline(deadline: Deadline) {
        _uiState.update {
            it.copy(selectedDeadline = deadline)
        }
    }

    fun addDeadline(
        title: String,
        description: String?,
        dueDate: LocalDate,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val project = projectRepository.getActiveProject().first()
                if (project != null) {
                    val newDeadline = Deadline(
                        projectId = project.id,
                        title = title.trim(),
                        description = description?.trim(),
                        dueDate = dueDate,
                        isCompleted = false,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                    repository.createDeadline(newDeadline)
                    onSuccess()
                } else {
                    _uiState.update { it.copy(errorMessage = "No active project selected") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to add deadline") }
            }
        }
    }

    fun editDeadline(
        deadline: Deadline,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.updateDeadline(deadline)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update deadline") }
            }
        }
    }

    fun toggleCompletion(
        deadline: Deadline,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val updated = deadline.copy(isCompleted = !deadline.isCompleted)
                repository.updateDeadline(updated)
                // If we have a selectedDeadline currently, update it too
                if (_uiState.value.selectedDeadline?.id == deadline.id) {
                    _uiState.update { it.copy(selectedDeadline = updated) }
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to toggle deadline status") }
            }
        }
    }

    fun deleteDeadline(
        deadline: Deadline,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteDeadline(deadline)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to delete deadline") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
