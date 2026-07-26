package com.devflow.app.features.project.presentation.state

import com.devflow.app.features.project.domain.model.Project

data class ProjectUiState(
    val projectList: List<Project> = emptyList(),
    val loadingState: Boolean = false,
    val errorMessage: String? = null,
    val selectedProject: Project? = null,
    val activeProject: Project? = null,
    val showArchived: Boolean = false
)
