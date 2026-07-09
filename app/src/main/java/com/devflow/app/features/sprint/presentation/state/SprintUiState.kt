package com.devflow.app.features.sprint.presentation.state

import com.devflow.app.features.sprint.domain.model.Sprint
import com.devflow.app.features.tasks.domain.model.Task
import com.devflow.app.features.stories.domain.model.UserStory

data class SprintUiState(
    val sprintList: List<Sprint> = emptyList(),
    val selectedSprint: Sprint? = null,
    val assignedTasks: List<Task> = emptyList(),
    val assignedStories: List<UserStory> = emptyList(),
    val loadingState: Boolean = false,
    val errorMessage: String? = null
)
