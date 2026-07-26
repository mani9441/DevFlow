package com.devflow.app.features.stories.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.stories.domain.model.UserStory
import com.devflow.app.features.stories.domain.model.StoryPriority
import com.devflow.app.features.stories.domain.repository.UserStoryRepository
import com.devflow.app.features.stories.presentation.state.StoryUiState
import com.devflow.app.features.sprint.domain.repository.SprintRepository
import com.devflow.app.features.project.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class StoryViewModel @Inject constructor(
    private val repository: UserStoryRepository,
    private val sprintRepository: SprintRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(StoryUiState())
    val uiState: StateFlow<StoryUiState> = _uiState.asStateFlow()

    init {
        loadStories()
        loadAvailableSprints()
    }

    fun loadStories() {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                projectRepository.getActiveProject().collect { project ->
                    if (project != null) {
                        launch {
                            repository.getAllStories(project.id).collect { stories ->
                                _uiState.update { it.copy(storyList = stories, loadingState = false, errorMessage = null) }
                            }
                        }
                        launch {
                            repository.getUnassignedStories(project.id).collect { stories ->
                                _uiState.update { it.copy(unassignedStories = stories) }
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                storyList = emptyList(),
                                unassignedStories = emptyList(),
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
                        errorMessage = e.message ?: "Failed to load stories"
                    )
                }
            }
        }
    }

    fun loadStory(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingState = true) }
            val story = repository.getStory(id)
            _uiState.update {
                it.copy(
                    selectedStory = story,
                    loadingState = false,
                    errorMessage = if (story == null) "Story not found" else null
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
                // Ignore
            }
        }
    }

    fun addStory(
        title: String,
        description: String?,
        acceptanceCriteria: String?,
        priority: StoryPriority,
        sprintId: Long?,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val project = projectRepository.getActiveProject().first()
                if (project != null) {
                    val newStory = UserStory(
                        projectId = project.id,
                        title = title.trim(),
                        description = description?.trim(),
                        acceptanceCriteria = acceptanceCriteria?.trim(),
                        priority = priority,
                        sprintId = sprintId,
                        createdAt = LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )
                    repository.createStory(newStory)
                    onSuccess()
                } else {
                    _uiState.update { it.copy(errorMessage = "No active project selected") }
                }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to add user story") }
            }
        }
    }

    fun editStory(
        story: UserStory,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.updateStory(story)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update user story") }
            }
        }
    }

    fun assignSprint(
        story: UserStory,
        sprintId: Long?,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val updated = story.copy(sprintId = sprintId)
                repository.updateStory(updated)
                if (_uiState.value.selectedStory?.id == story.id) {
                    _uiState.update { it.copy(selectedStory = updated) }
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to assign sprint") }
            }
        }
    }

    fun deleteStory(
        story: UserStory,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteStory(story)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to delete user story") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
