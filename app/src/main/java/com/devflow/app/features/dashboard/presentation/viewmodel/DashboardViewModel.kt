package com.devflow.app.features.dashboard.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.dashboard.presentation.state.DashboardUiState
import com.devflow.app.features.project.domain.repository.ProjectRepository
import com.devflow.app.features.sprint.domain.repository.SprintRepository
import com.devflow.app.features.meetings.domain.repository.MeetingRepository
import com.devflow.app.features.notes.domain.repository.NoteRepository
import com.devflow.app.features.issues.domain.model.IssueStatus
import com.devflow.app.features.issues.domain.repository.IssueRepository
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import com.devflow.app.features.monitoring.domain.repository.DevelopmentMonitoringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val projectRepository: ProjectRepository,
    private val sprintRepository: SprintRepository,
    private val meetingRepository: MeetingRepository,
    private val noteRepository: NoteRepository,
    private val issueRepository: IssueRepository,
    private val monitoringRepository: DevelopmentMonitoringRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(DashboardUiState())
    val uiState: StateFlow<DashboardUiState> = _uiState.asStateFlow()

    private var activeProjectJob: Job? = null

    init {
        observeActiveProject()
    }

    private fun observeActiveProject() {
        activeProjectJob?.cancel()
        activeProjectJob = viewModelScope.launch {
            projectRepository.getActiveProject().collect { project ->
                _uiState.update { it.copy(activeProject = project) }
                if (project != null) {
                    loadDashboardData(project.id)
                } else {
                    _uiState.update {
                        DashboardUiState(activeProject = null)
                    }
                }
            }
        }
    }

    private fun loadDashboardData(projectId: Long) {
        _uiState.update { it.copy(loading = true) }
        viewModelScope.launch {
            // Sprints
            launch {
                sprintRepository.getAllSprints(projectId).collect { sprints ->
                    val today = LocalDate.now()
                    val current = sprints.find {
                        (it.startDate.isBefore(today) || it.startDate.isEqual(today)) &&
                        (it.endDate.isAfter(today) || it.endDate.isEqual(today))
                    } ?: sprints.firstOrNull()

                    _uiState.update {
                        it.copy(
                            currentSprint = current,
                            sprintsCount = sprints.size
                        )
                    }
                }
            }

            // Meetings
            launch {
                meetingRepository.getAllMeetings(projectId).collect { meetings ->
                    val today = LocalDate.now()
                    val todayMeetings = meetings.filter { it.meetingDate == today }
                    _uiState.update { it.copy(meetings = todayMeetings) }
                }
            }

            // Notes
            launch {
                noteRepository.getAllNotes(projectId).collect { notes ->
                    val recent = notes.take(3)
                    _uiState.update {
                        it.copy(
                            recentNotes = recent,
                            notesCount = notes.size
                        )
                    }
                }
            }

            // Issues
            launch {
                issueRepository.getAllIssues(projectId).collect { issues ->
                    val openIssues = issues.filter { it.status == IssueStatus.OPEN || it.status == IssueStatus.IN_PROGRESS }
                    _uiState.update { it.copy(issues = openIssues) }
                }
            }

            // Repo config & Build Status
            launch {
                monitoringRepository.getRepository(projectId).collect { config ->
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
        val active = _uiState.value.activeProject
        if (active != null) {
            loadDashboardData(active.id)
        }
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
