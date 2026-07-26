package com.devflow.app.features.sprint.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.sprint.domain.model.Sprint
import com.devflow.app.features.sprint.domain.repository.SprintRepository
import com.devflow.app.features.sprint.presentation.state.SprintUiState
import com.devflow.app.features.tasks.domain.repository.TaskRepository
import com.devflow.app.features.stories.domain.repository.UserStoryRepository
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
class SprintViewModel @Inject constructor(
    private val repository: SprintRepository,
    private val taskRepository: TaskRepository,
    private val storyRepository: UserStoryRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(SprintUiState())
    val uiState: StateFlow<SprintUiState> = _uiState.asStateFlow()

    init {
        loadSprints()
    }

    fun loadSprints() {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                projectRepository.getActiveProject().collect { project ->
                    if (project != null) {
                        repository.getAllSprints(project.id).collect { sprints ->
                            _uiState.update {
                                it.copy(
                                    sprintList = sprints,
                                    loadingState = false,
                                    errorMessage = null
                                )
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                sprintList = emptyList(),
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
                        errorMessage = e.message ?: "Failed to load sprints"
                    )
                }
            }
        }
    }

    fun loadSprint(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingState = true) }
            val sprint = repository.getSprint(id)
            if (sprint != null) {
                // Collect tasks and stories for this sprint
                launch {
                    taskRepository.getTasksBySprint(id).collect { tasks ->
                        _uiState.update { it.copy(assignedTasks = tasks) }
                    }
                }
                launch {
                    storyRepository.getStoriesBySprint(id).collect { stories ->
                        _uiState.update { it.copy(assignedStories = stories) }
                    }
                }
            }
            _uiState.update {
                it.copy(
                    selectedSprint = sprint,
                    loadingState = false,
                    errorMessage = if (sprint == null) "Sprint not found" else null
                )
            }
        }
    }

    fun addSprint(
        name: String,
        goal: String,
        startDate: LocalDate,
        endDate: LocalDate,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val project = projectRepository.getActiveProject().first()
                if (project != null) {
                    val newSprint = Sprint(
                        projectId = project.id,
                        name = name.trim(),
                        goal = goal.trim(),
                        startDate = startDate,
                        endDate = endDate,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                    repository.createSprint(newSprint)
                    onSuccess()
                } else {
                    _uiState.update { it.copy(errorMessage = "No active project selected") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to add sprint") }
            }
        }
    }

    fun editSprint(
        sprint: Sprint,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.updateSprint(sprint)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update sprint") }
            }
        }
    }

    fun deleteSprint(
        sprint: Sprint,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteSprint(sprint)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to delete sprint") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
