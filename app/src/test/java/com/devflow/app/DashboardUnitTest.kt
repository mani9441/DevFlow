package com.devflow.app

import com.devflow.app.features.dashboard.presentation.viewmodel.DashboardViewModel
import com.devflow.app.features.deadlines.domain.model.Deadline
import com.devflow.app.features.deadlines.domain.repository.DeadlineRepository
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.issues.domain.model.IssueStatus
import com.devflow.app.features.issues.domain.repository.IssueRepository
import com.devflow.app.features.meetings.domain.model.Meeting
import com.devflow.app.features.meetings.domain.repository.MeetingRepository
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import com.devflow.app.features.monitoring.domain.model.WorkflowRun
import com.devflow.app.features.monitoring.domain.repository.DevelopmentMonitoringRepository
import com.devflow.app.features.todo.domain.model.Todo
import com.devflow.app.features.todo.domain.model.TodoStatus
import com.devflow.app.features.todo.domain.repository.TodoRepository
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
    private val mockTodos = listOf(
        Todo(id = 1, title = "Task 1", status = TodoStatus.PENDING, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Todo(id = 2, title = "Task 2", status = TodoStatus.PENDING, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Todo(id = 3, title = "Task 3", status = TodoStatus.COMPLETED, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Todo(id = 4, title = "Task 4", status = TodoStatus.PENDING, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Todo(id = 5, title = "Task 5", status = TodoStatus.PENDING, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())
    )

    private val mockMeetings = listOf(
        Meeting(id = 1, title = "Today meeting", meetingDate = LocalDate.now(), meetingTime = "9:30 AM", yesterdayWork = "", todayPlan = "", blockers = "", createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Meeting(id = 2, title = "Tomorrow meeting", meetingDate = LocalDate.now().plusDays(1), meetingTime = "10:00 AM", yesterdayWork = "", todayPlan = "", blockers = "", createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())
    )

    private val mockDeadlines = listOf(
        Deadline(id = 1, title = "Deadline Far", dueDate = LocalDate.now().plusDays(10), createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Deadline(id = 2, title = "Deadline Near", dueDate = LocalDate.now().plusDays(2), createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Deadline(id = 3, title = "Completed Deadline", dueDate = LocalDate.now().plusDays(1), isCompleted = true, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())
    )

    private val mockIssues = listOf(
        Issue(id = 1, title = "Issue Open", status = IssueStatus.OPEN, priority = IssuePriority.HIGH, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Issue(id = 2, title = "Issue Resolved", status = IssueStatus.RESOLVED, priority = IssuePriority.MEDIUM, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now()),
        Issue(id = 3, title = "Issue In Progress", status = IssueStatus.IN_PROGRESS, priority = IssuePriority.CRITICAL, createdAt = LocalDateTime.now(), updatedAt = LocalDateTime.now())
    )

    // Stub repositories
    private val todoRepo = object : TodoRepository {
        override suspend fun createTodo(todo: Todo): Long = 0L
        override suspend fun updateTodo(todo: Todo) {}
        override suspend fun deleteTodo(todo: Todo) {}
        override suspend fun markCompleted(id: Long) {}
        override suspend fun getTodo(id: Long): Todo? = null
        override fun getAllTodos(): Flow<List<Todo>> = flowOf(mockTodos)
    }

    private val meetingRepo = object : MeetingRepository {
        override suspend fun createMeeting(meeting: Meeting): Long = 0L
        override suspend fun updateMeeting(meeting: Meeting) {}
        override suspend fun deleteMeeting(meeting: Meeting) {}
        override suspend fun getMeeting(id: Long): Meeting? = null
        override fun getAllMeetings(): Flow<List<Meeting>> = flowOf(mockMeetings)
    }

    private val deadlineRepo = object : DeadlineRepository {
        override suspend fun createDeadline(deadline: Deadline): Long = 0L
        override suspend fun updateDeadline(deadline: Deadline) {}
        override suspend fun deleteDeadline(deadline: Deadline) {}
        override suspend fun getDeadline(id: Long): Deadline? = null
        override fun getAllDeadlines(): Flow<List<Deadline>> = flowOf(mockDeadlines)
    }

    private val issueRepo = object : IssueRepository {
        override suspend fun createIssue(issue: Issue): Long = 0L
        override suspend fun updateIssue(issue: Issue) {}
        override suspend fun deleteIssue(issue: Issue) {}
        override suspend fun getIssue(id: Long): Issue? = null
        override fun getAllIssues(): Flow<List<Issue>> = flowOf(mockIssues)
    }

    private val config = RepositoryConfig(1L, "google", "gson", null, LocalDateTime.now(), LocalDateTime.now())

    private val monitoringRepo = object : DevelopmentMonitoringRepository {
        override suspend fun saveRepository(config: RepositoryConfig): Long = 0L
        override fun getRepository(): Flow<RepositoryConfig?> = flowOf(config)
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
            todoRepository = todoRepo,
            meetingRepository = meetingRepo,
            deadlineRepository = deadlineRepo,
            issueRepository = issueRepo,
            monitoringRepository = monitoringRepo
        )

        advanceUntilIdle()

        val state = viewModel.uiState.value

        // Check Todos (Filter pending, take 3)
        assertEquals(3, state.todos.size)
        state.todos.forEach {
            assertEquals(TodoStatus.PENDING, it.status)
        }

        // Check Meetings (Filter today)
        assertEquals(1, state.meetings.size)
        assertEquals("Today meeting", state.meetings.first().title)

        // Check Deadlines (Filter uncompleted, sort by date)
        assertEquals(2, state.deadlines.size)
        assertEquals("Deadline Near", state.deadlines.first().title) // Sorted nearest first

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
