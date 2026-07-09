package com.devflow.app.features.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.dashboard.presentation.state.DashboardUiState
import com.devflow.app.features.deadlines.domain.repository.DeadlineRepository
import com.devflow.app.features.issues.domain.model.IssueStatus
import com.devflow.app.features.issues.domain.repository.IssueRepository
import com.devflow.app.features.meetings.domain.repository.MeetingRepository
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import com.devflow.app.features.monitoring.domain.repository.DevelopmentMonitoringRepository
import com.devflow.app.features.todo.domain.model.TodoStatus
import com.devflow.app.features.todo.domain.repository.TodoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val todoRepository: TodoRepository,
    private val meetingRepository: MeetingRepository,
    private val deadlineRepository: DeadlineRepository,
    private val issueRepository: IssueRepository,
    private val monitoringRepository: DevelopmentMonitoringRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    init {
        loadDashboard()
    }

    fun loadDashboard() {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            // Launch parallel collection of repositories
            launch {
                todoRepository.getAllTodos().collect { todos ->
                    val pending = todos.filter { it.status == TodoStatus.PENDING }.take(3)
                    _uiState.update { it.copy(todos = pending) }
                }
            }

            launch {
                meetingRepository.getAllMeetings().collect { meetings ->
                    val today = LocalDate.now()
                    val todayMeetings = meetings.filter { it.meetingDate == today }
                    _uiState.update { it.copy(meetings = todayMeetings) }
                }
            }

            launch {
                deadlineRepository.getAllDeadlines().collect { deadlines ->
                    val today = LocalDate.now()
                    val upcoming = deadlines
                        .filter { !it.isCompleted && (it.dueDate.isAfter(today) || it.dueDate.isEqual(today)) }
                        .sortedBy { it.dueDate }
                    _uiState.update { it.copy(deadlines = upcoming) }
                }
            }

            launch {
                issueRepository.getAllIssues().collect { issues ->
                    val openIssues = issues.filter { it.status == IssueStatus.OPEN || it.status == IssueStatus.IN_PROGRESS }
                    _uiState.update { it.copy(issues = openIssues) }
                }
            }

            launch {
                monitoringRepository.getRepository().collect { config ->
                    _uiState.update { it.copy(repoConfig = config, loading = false) }
                    if (config != null) {
                        fetchLatestBuild(config)
                    } else {
                        _uiState.update { it.copy(latestRun = null) }
                    }
                }
            }
        }
    }

    fun refreshDashboard() {
        loadDashboard()
    }

    private fun fetchLatestBuild(config: RepositoryConfig) {
        viewModelScope.launch {
            try {
                val runs = monitoringRepository.loadBuildStatus(
                    owner = config.owner,
                    repo = config.repository,
                    token = config.personalAccessToken
                )
                _uiState.update { it.copy(latestRun = runs.firstOrNull(), errorMessage = null) }
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = "Build Status Fetch Error: ${e.message}") }
            }
        }
    }
}
