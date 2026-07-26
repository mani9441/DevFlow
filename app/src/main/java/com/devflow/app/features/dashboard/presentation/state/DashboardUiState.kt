package com.devflow.app.features.dashboard.presentation.state

import com.devflow.app.features.project.domain.model.Project
import com.devflow.app.features.sprint.domain.model.Sprint
import com.devflow.app.features.meetings.domain.model.Meeting
import com.devflow.app.features.notes.domain.model.Note
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.monitoring.domain.model.WorkflowRun
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig

data class DashboardUiState(
    val activeProject: Project? = null,
    val currentSprint: Sprint? = null,
    val meetings: List<Meeting> = emptyList(),
    val recentNotes: List<Note> = emptyList(),
    val issues: List<Issue> = emptyList(),
    val latestRun: WorkflowRun? = null,
    val repoConfig: RepositoryConfig? = null,
    val loading: Boolean = false,
    val errorMessage: String? = null,
    val sprintsCount: Int = 0,
    val notesCount: Int = 0
)
