package com.devflow.app.features.tasks.presentation.state

import com.devflow.app.features.tasks.domain.model.Task
import com.devflow.app.features.sprint.domain.model.Sprint

data class TaskUiState(
    val taskList: List<Task> = emptyList(),
    val unassignedTasks: List<Task> = emptyList(),
    val selectedTask: Task? = null,
    val availableSprints: List<Sprint> = emptyList(),
    val loadingState: Boolean = false,
    val errorMessage: String? = null
)
