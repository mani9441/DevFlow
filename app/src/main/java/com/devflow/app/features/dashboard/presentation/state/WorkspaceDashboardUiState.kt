package com.devflow.app.features.dashboard.presentation.state

import com.devflow.app.features.todo.domain.model.Todo
import com.devflow.app.features.project.domain.model.Project
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.issues.domain.model.IssueStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class WorkspaceDashboardUiState(
    val todayMeetings: List<WorkspaceMeeting> = emptyList(),
    val todayDeadlines: List<WorkspaceDeadline> = emptyList(),
    val todayTodos: List<Todo> = emptyList(),
    val highPriorityIssues: List<WorkspaceIssue> = emptyList(),
    val failedBuilds: List<WorkspaceBuild> = emptyList(),
    val activeProjects: List<WorkspaceProjectOverview> = emptyList(),
    val recentActivity: List<WorkspaceActivity> = emptyList(),
    val latestBuilds: List<WorkspaceBuild> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

data class WorkspaceMeeting(
    val id: Long,
    val title: String,
    val meetingTime: String,
    val projectName: String,
    val projectId: Long
)

data class WorkspaceDeadline(
    val id: Long,
    val title: String,
    val dueDate: LocalDate,
    val projectName: String,
    val projectId: Long
)

data class WorkspaceIssue(
    val id: Long,
    val title: String,
    val priority: IssuePriority,
    val status: IssueStatus,
    val projectName: String,
    val projectId: Long
)

data class WorkspaceBuild(
    val projectName: String,
    val projectId: Long,
    val workflowName: String,
    val runNumber: Int,
    val branch: String,
    val conclusion: String
)

data class WorkspaceProjectOverview(
    val project: Project,
    val activeSprintName: String?,
    val activeSprintProgress: Int // Percentage e.g. 68%
)

data class WorkspaceActivity(
    val title: String, // e.g., "Sprint Created", "New Issue"
    val description: String,
    val projectName: String,
    val projectId: Long,
    val timestamp: LocalDateTime
)
