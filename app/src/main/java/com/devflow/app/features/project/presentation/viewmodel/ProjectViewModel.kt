package com.devflow.app.features.project.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.project.domain.model.Project
import com.devflow.app.features.project.domain.model.ProjectStatus
import com.devflow.app.features.project.domain.repository.ProjectRepository
import com.devflow.app.features.project.presentation.state.ProjectUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val repository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProjectUiState())
    val uiState: StateFlow<ProjectUiState> = _uiState.asStateFlow()

    init {
        loadProjects()
        loadActiveProject()
    }

    fun loadProjects() {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                repository.getAllProjects().collect { projects ->
                    _uiState.update {
                        it.copy(
                            projectList = projects,
                            loadingState = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingState = false,
                        errorMessage = e.message ?: "Failed to load projects"
                    )
                }
            }
        }
    }

    fun loadActiveProject() {
        viewModelScope.launch {
            try {
                repository.getActiveProject().collect { active ->
                    _uiState.update { it.copy(activeProject = active) }
                }
            } catch (e: Exception) {
                // Ignore
            }
        }
    }

    fun loadProject(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingState = true) }
            val project = repository.getProject(id)
            _uiState.update {
                it.copy(
                    selectedProject = project,
                    loadingState = false,
                    errorMessage = if (project == null) "Project not found" else null
                )
            }
        }
    }

    fun createProject(
        name: String,
        description: String?,
        startDate: LocalDate?,
        endDate: LocalDate?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val newProject = Project(
                    name = name.trim(),
                    description = description?.trim(),
                    status = ProjectStatus.PLANNING,
                    startDate = startDate,
                    endDate = endDate,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                repository.createProject(newProject)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to create project") }
            }
        }
    }

    fun editProject(
        project: Project,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.updateProject(project)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update project") }
            }
        }
    }

    fun deleteProject(
        project: Project,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteProject(project)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to delete project") }
            }
        }
    }

    fun archiveProject(
        id: Long,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.archiveProject(id)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to archive project") }
            }
        }
    }

    fun selectProject(
        project: Project,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.setActiveProject(project.id)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to set active project") }
            }
        }
    }

    fun toggleShowArchived() {
        _uiState.update { it.copy(showArchived = !it.showArchived) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
