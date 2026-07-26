package com.devflow.app

import com.devflow.app.features.dashboard.presentation.viewmodel.DashboardViewModel
import com.devflow.app.features.project.domain.model.Project
import com.devflow.app.features.project.domain.model.ProjectStatus
import com.devflow.app.features.project.domain.repository.ProjectRepository
import com.devflow.app.features.sprint.domain.model.Sprint
import com.devflow.app.features.sprint.domain.repository.SprintRepository
import com.devflow.app.features.meetings.domain.model.Meeting
import com.devflow.app.features.meetings.domain.repository.MeetingRepository
import com.devflow.app.features.notes.domain.model.Note
import com.devflow.app.features.notes.domain.repository.NoteRepository
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.issues.domain.model.IssueStatus
import com.devflow.app.features.issues.domain.repository.IssueRepository
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import com.devflow.app.features.monitoring.domain.model.WorkflowRun
import com.devflow.app.features.monitoring.domain.repository.DevelopmentMonitoringRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Before
import org.junit.Test
import java.time.LocalDate
import java.time.LocalDateTime

@OptIn(ExperimentalCoroutinesApi::class)
class DashboardUnitTest {

    private val testDispatcher = StandardTestDispatcher()

    // Mock data structures
    private val mockProject = Project(
        id = 1L,
        name = "DevFlow Test",
        description = "Dashboard Test Project",
        status = ProjectStatus.ACTIVE,
        startDate = LocalDate.now().minusDays(5),
        endDate = LocalDate.now().plusMonths(1),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    private val mockSprint = Sprint(
        id = 1L,
        projectId = 1L,
        name = "Sprint 1",
        goal = "Dashboard Goal",
        startDate = LocalDate.now().minusDays(2),
        endDate = LocalDate.now().plusDays(5),
        createdAt = LocalDateTime.now(),
        updatedAt = LocalDateTime.now()
    )

    private val mockMeetings = listOf(
        Meeting(id = 1, projectId = 1L, title = "Today meeting", meetingDate = LocalDate.now(), meetingTime = "9:30 AM", yesterdayWork = "", todayPlan = "", blockers = "", createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Meeting(id = 2, projectId = 1L, title = "Tomorrow meeting", meetingDate = LocalDate.now().plusDays(1), meetingTime = "10:00 AM", yesterdayWork = "", todayPlan = "", blockers = "", createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())
    )

    private val mockNotes = listOf(
        Note(id = 1, projectId = 1L, title = "Note 1", content = "Content 1", createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Note(id = 2, projectId = 1L, title = "Note 2", content = "Content 2", createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())
    )

    private val mockIssues = listOf(
        Issue(id = 1, projectId = 1L, title = "Issue Open", status = IssueStatus.OPEN, priority = IssuePriority.HIGH, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Issue(id = 2, projectId = 1L, title = "Issue Resolved", status = IssueStatus.RESOLVED, priority = IssuePriority.MEDIUM, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Issue(id = 3, projectId = 1L, title = "Issue In Progress", status = IssueStatus.IN_PROGRESS, priority = IssuePriority.CRITICAL, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())
    )

    // Stub repositories
    private val projectRepo = object : ProjectRepository {
        override suspend fun createProject(project: Project): Long = 0L
        override suspend fun updateProject(project: Project) {}
        override suspend fun deleteProject(project: Project) {}
        override suspend fun getProject(id: Long): Project? = mockProject
        override fun getAllProjects(): Flow<List<Project>> = flowOf(listOf(mockProject))
        override fun getActiveProject(): Flow<Project?> = flowOf(mockProject)
        override suspend fun setActiveProject(projectId: Long?) {}
        override suspend fun archiveProject(id: Long) {}
    }

    private val sprintRepo = object : SprintRepository {
        override suspend fun createSprint(sprint: Sprint): Long = 0L
        override suspend fun updateSprint(sprint: Sprint) {}
        override suspend fun deleteSprint(sprint: Sprint) {}
        override suspend fun getSprint(id: Long): Sprint? = mockSprint
        override fun getAllSprints(projectId: Long): Flow<List<Sprint>> = flowOf(listOf(mockSprint))
    }

    private val meetingRepo = object : MeetingRepository {
        override suspend fun createMeeting(meeting: Meeting): Long = 0L
        override suspend fun updateMeeting(meeting: Meeting) {}
        override suspend fun deleteMeeting(meeting: Meeting) {}
        override suspend fun getMeeting(id: Long): Meeting? = null
        override fun getAllMeetings(projectId: Long): Flow<List<Meeting>> = flowOf(mockMeetings)
    }

    private val noteRepo = object : NoteRepository {
        override suspend fun createNote(note: Note): Long = 0L
        override suspend fun updateNote(note: Note) {}
        override suspend fun deleteNote(note: Note) {}
        override suspend fun getNote(id: Long): Note? = null
        override fun getAllNotes(projectId: Long): Flow<List<Note>> = flowOf(mockNotes)
    }

    private val issueRepo = object : IssueRepository {
        override suspend fun createIssue(issue: Issue): Long = 0L
        override suspend fun updateIssue(issue: Issue) {}
        override suspend fun deleteIssue(issue: Issue) {}
        override suspend fun getIssue(id: Long): Issue? = null
        override fun getAllIssues(projectId: Long): Flow<List<Issue>> = flowOf(mockIssues)
    }

    private val config = RepositoryConfig(1L, 1L, "google", "gson", null, LocalDateTime.now(), LocalDateTime.now())

    private val monitoringRepo = object : DevelopmentMonitoringRepository {
        override suspend fun saveRepository(config: RepositoryConfig): Long = 0L
        override fun getRepository(projectId: Long): Flow<RepositoryConfig?> = flowOf(config)
        override suspend fun deleteRepository(config: RepositoryConfig) {}
        override suspend fun loadBuildStatus(owner: String, repo: String, token: String?): List<WorkflowRun> {
            return listOf(
                WorkflowRun(1L, "CI Build", "completed", "success", "master", 101, "push", "", "", "")
            )
        }
    }

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun dashboardViewModel_aggregatesDataCorrectly() = runTest(testDispatcher) {
        val viewModel = DashboardViewModel(
            projectRepository = projectRepo,
            sprintRepository = sprintRepo,
            meetingRepository = meetingRepo,
            noteRepository = noteRepo,
            issueRepository = issueRepo,
            monitoringRepository = monitoringRepo
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        // Check active project
        assertNotNull(state.activeProject)
        assertEquals("DevFlow Test", state.activeProject?.name)

        // Check Sprints
        assertNotNull(state.currentSprint)
        assertEquals("Sprint 1", state.currentSprint?.name)
        assertEquals(1, state.sprintsCount)

        // Check Meetings (Filter today)
        assertEquals(1, state.meetings.size)
        assertEquals("Today meeting", state.meetings.first().title)

        // Check Notes
        assertEquals(2, state.recentNotes.size)
        assertEquals(2, state.notesCount)

        // Check Issues (Filter OPEN and IN_PROGRESS)
        assertEquals(2, state.issues.size)
        val statuses = state.issues.map { it.status }
        assertEquals(listOf(IssueStatus.OPEN, IssueStatus.IN_PROGRESS), statuses)

        // Check build status
        assertNotNull(state.latestRun)
        assertEquals("CI Build", state.latestRun?.workflowName)
        assertEquals("success", state.latestRun?.conclusion)
    }
}
