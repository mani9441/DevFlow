package com.devflow.app.features.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.deadlines.domain.model.Deadline
import com.devflow.app.features.deadlines.domain.repository.DeadlineRepository
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.issues.domain.model.IssueStatus
import com.devflow.app.features.issues.domain.repository.IssueRepository
import com.devflow.app.features.meetings.domain.model.Meeting
import com.devflow.app.features.meetings.domain.repository.MeetingRepository
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import com.devflow.app.features.monitoring.domain.repository.DevelopmentMonitoringRepository
import com.devflow.app.features.notes.domain.model.Note
import com.devflow.app.features.notes.domain.repository.NoteRepository
import com.devflow.app.features.project.domain.model.Project
import com.devflow.app.features.project.domain.model.ProjectStatus
import com.devflow.app.features.project.domain.repository.ProjectRepository
import com.devflow.app.features.sprint.domain.model.Sprint
import com.devflow.app.features.sprint.domain.repository.SprintRepository
import com.devflow.app.features.tasks.domain.model.Task
import com.devflow.app.features.tasks.domain.model.TaskStatus
import com.devflow.app.features.tasks.domain.repository.TaskRepository
import com.devflow.app.features.todo.domain.model.Todo
import com.devflow.app.features.todo.domain.repository.TodoRepository
import com.devflow.app.features.dashboard.presentation.state.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

private data class TodayFocusData(
    val todos: List<Todo>,
    val meetings: List<Meeting>,
    val deadlines: List<Deadline>,
    val issues: List<Issue>
)

private data class ProjectData(
    val projects: List<Project>,
    val sprints: List<Sprint>,
    val tasks: List<Task>,
    val notes: List<Note>
)

@HiltViewModel
class WorkspaceDashboardViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val todoRepository: TodoRepository,
    private val meetingRepository: MeetingRepository,
    private val deadlineRepository: DeadlineRepository,
    private val issueRepository: IssueRepository,
    private val sprintRepository: SprintRepository,
    private val taskRepository: TaskRepository,
    private val noteRepository: NoteRepository,
    private val monitoringRepository: DevelopmentMonitoringRepository
) : ViewModel() {

    private val _latestBuildsFlow = MutableStateFlow<List<WorkspaceBuild>>(emptyList())
    private val _isLoading = MutableStateFlow(false)
    private val _error = MutableStateFlow<String?>(null)

    private val todayFocusFlow = combine(
        todoRepository.getAllTodos(),
        meetingRepository.getAllMeetings(),
        deadlineRepository.getAllDeadlines(),
        issueRepository.getAllIssues()
    ) { todos, meetings, deadlines, issues ->
        TodayFocusData(todos, meetings, deadlines, issues)
    }

    private val projectDataFlow = combine(
        projectRepository.getAllProjects(),
        sprintRepository.getAllSprints(),
        taskRepository.getAllTasks(),
        noteRepository.getAllNotes()
    ) { projects, sprints, tasks, notes ->
        ProjectData(projects, sprints, tasks, notes)
    }

    val uiState: StateFlow<WorkspaceDashboardUiState> = combine(
        projectDataFlow,
        todayFocusFlow,
        _latestBuildsFlow,
        _isLoading,
        _error
    ) { projectData, todayFocus, builds, loading, err ->
        val projects = projectData.projects
        val sprints = projectData.sprints
        val tasks = projectData.tasks
        val notes = projectData.notes

        val todos = todayFocus.todos
        val meetings = todayFocus.meetings
        val deadlines = todayFocus.deadlines
        val issues = todayFocus.issues

        val projectMap = projects.associateBy { it.id }
        val today = LocalDate.now()

        // 1. Today's Meetings
        val todayMeetings = meetings.filter { it.meetingDate == today }.map {
            WorkspaceMeeting(
                id = it.id,
                title = it.title,
                meetingTime = it.meetingTime,
                projectName = projectMap[it.projectId]?.name ?: "Global",
                projectId = it.projectId
            )
        }

        // 2. Today's Deadlines (due today or tomorrow)
        val todayDeadlines = deadlines.filter { it.dueDate == today || it.dueDate == today.plusDays(1) }.map {
            WorkspaceDeadline(
                id = it.id,
                title = it.title,
                dueDate = it.dueDate,
                projectName = projectMap[it.projectId]?.name ?: "Global",
                projectId = it.projectId
            )
        }

        // 3. Pending Personal Tasks
        val todayTodos = todos.filter { it.status == com.devflow.app.features.todo.domain.model.TodoStatus.PENDING }

        // 4. High Priority Issues
        val highPriorityIssues = issues.filter {
            (it.priority == IssuePriority.CRITICAL || it.priority == IssuePriority.HIGH) &&
            it.status != IssueStatus.CLOSED && it.status != IssueStatus.RESOLVED
        }.map {
            WorkspaceIssue(
                id = it.id,
                title = it.title,
                priority = it.priority,
                status = it.status,
                projectName = projectMap[it.projectId]?.name ?: "Global",
                projectId = it.projectId
            )
        }

        // 5. Active Projects Overview with Sprint progress
        val activeProjects = projects.filter { it.status != ProjectStatus.ARCHIVED }.map { proj ->
            val projSprints = sprints.filter { it.projectId == proj.id }
            val activeSprint = projSprints.find { sprint ->
                val start = sprint.startDate
                val end = sprint.endDate
                !today.isBefore(start) && !today.isAfter(end)
            } ?: projSprints.firstOrNull() // Fallback to latest sprint

            val activeSprintName = activeSprint?.name
            val progress = if (activeSprint != null) {
                val sprintTasks = tasks.filter { it.projectId == proj.id && it.sprintId == activeSprint.id }
                if (sprintTasks.isEmpty()) 0 else {
                    val completed = sprintTasks.count { it.status == TaskStatus.COMPLETED }
                    (completed * 100) / sprintTasks.size
                }
            } else 0

            WorkspaceProjectOverview(
                project = proj,
                activeSprintName = activeSprintName,
                activeSprintProgress = progress
            )
        }

        // 6. Recent Activity consolidation
        val activityList = mutableListOf<WorkspaceActivity>()
        
        // Add Sprints created recently
        sprints.forEach { sprint ->
            activityList.add(
                WorkspaceActivity(
                    title = "Sprint Created",
                    description = sprint.name,
                    projectName = projectMap[sprint.projectId]?.name ?: "Global",
                    projectId = sprint.projectId,
                    timestamp = sprint.createdAt
                )
            )
        }
        // Add Issues logged recently
        issues.forEach { issue ->
            activityList.add(
                WorkspaceActivity(
                    title = "New Issue",
                    description = issue.title,
                    projectName = projectMap[issue.projectId]?.name ?: "Global",
                    projectId = issue.projectId,
                    timestamp = issue.createdAt
                )
            )
        }
        // Add Notes logged recently
        notes.forEach { note ->
            activityList.add(
                WorkspaceActivity(
                    title = "New Note",
                    description = note.title,
                    projectName = projectMap[note.projectId]?.name ?: "Global",
                    projectId = note.projectId,
                    timestamp = note.createdAt
                )
            )
        }
        // Add Meetings logged recently
        meetings.forEach { meeting ->
            activityList.add(
                WorkspaceActivity(
                    title = "New Meeting",
                    description = meeting.title,
                    projectName = projectMap[meeting.projectId]?.name ?: "Global",
                    projectId = meeting.projectId,
                    timestamp = meeting.createdAt
                )
            )
        }

        val recentActivity = activityList.sortedByDescending { it.timestamp }.take(6)

        // 7. Failed builds
        val failedBuilds = builds.filter { it.conclusion == "failure" || it.conclusion == "cancelled" }

        WorkspaceDashboardUiState(
            todayMeetings = todayMeetings,
            todayDeadlines = todayDeadlines,
            todayTodos = todayTodos,
            highPriorityIssues = highPriorityIssues,
            failedBuilds = failedBuilds,
            activeProjects = activeProjects,
            recentActivity = recentActivity,
            latestBuilds = builds,
            isLoading = loading,
            error = err
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = WorkspaceDashboardUiState(isLoading = true)
    )

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        _isLoading.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                monitoringRepository.getAllRepositoryConfigs().collect { configs ->
                    fetchBuilds(configs)
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                _error.value = "Failed to load configs: ${e.localizedMessage}"
                _isLoading.value = false
            }
        }
    }

    fun refreshDashboard() {
        loadDashboard()
    }

    private fun fetchBuilds(configs: List<RepositoryConfig>) {
        viewModelScope.launch {
            val projectList = projectRepository.getAllProjects().first()
            val projectMap = projectList.associateBy { it.id }
            val list = mutableListOf<WorkspaceBuild>()
            configs.forEach { config ->
                try {
                    val runs = monitoringRepository.loadBuildStatus(config.owner, config.repository, config.personalAccessToken)
                    val latest = runs.firstOrNull()
                    if (latest != null) {
                        list.add(
                            WorkspaceBuild(
                                projectName = projectMap[config.projectId]?.name ?: "Unknown Project",
                                projectId = config.projectId,
                                workflowName = latest.workflowName,
                                runNumber = latest.runNumber,
                                branch = latest.branch,
                                conclusion = latest.conclusion
                            )
                        )
                    }
                } catch (e: Exception) {
                    // Ignore config network errors in global overview to keep it resilient
                }
            }
            _latestBuildsFlow.value = list
        }
    }

    fun selectProject(project: Project, onComplete: () -> Unit) {
        viewModelScope.launch {
            projectRepository.setActiveProject(project.id)
            onComplete()
        }
    }
}
