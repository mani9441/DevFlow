package com.devflow.app.features.dashboard.presentation.state

import com.devflow.app.features.deadlines.domain.model.Deadline
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.meetings.domain.model.Meeting
import com.devflow.app.features.monitoring.domain.model.WorkflowRun
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import com.devflow.app.features.todo.domain.model.Todo

data class DashboardUiState(
    val meetings: List<Meeting> = emptyList(),
    val deadlines: List<Deadline> = emptyList(),
    val todos: List<Todo> = emptyList(),
    val issues: List<Issue> = emptyList(),
    val latestRun: WorkflowRun? = null,
    val repoConfig: RepositoryConfig? = null,
    val loading: Boolean = false,
    val errorMessage: String? = null
)
