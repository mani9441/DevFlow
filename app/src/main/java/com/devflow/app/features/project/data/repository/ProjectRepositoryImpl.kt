package com.devflow.app.features.project.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.devflow.app.features.project.data.local.dao.ProjectDao
import com.devflow.app.features.project.data.mapper.toDomain
import com.devflow.app.features.project.data.mapper.toEntity
import com.devflow.app.features.project.domain.model.Project
import com.devflow.app.features.project.domain.model.ProjectStatus
import com.devflow.app.features.project.domain.repository.ProjectRepository
import com.devflow.app.features.project.data.local.entity.ProjectEntity
import com.devflow.app.features.communication.data.local.dao.TeamMemberDao
import com.devflow.app.features.communication.data.local.entity.TeamMemberEntity
import com.devflow.app.features.sprint.data.local.dao.SprintDao
import com.devflow.app.features.sprint.data.local.entity.SprintEntity
import com.devflow.app.features.tasks.data.local.dao.TaskDao
import com.devflow.app.features.tasks.data.local.entity.TaskEntity
import com.devflow.app.features.tasks.domain.model.TaskStatus
import com.devflow.app.features.tasks.domain.model.TaskPriority
import com.devflow.app.features.stories.data.local.dao.UserStoryDao
import com.devflow.app.features.stories.data.local.entity.UserStoryEntity
import com.devflow.app.features.stories.domain.model.StoryPriority
import com.devflow.app.features.meetings.data.local.dao.MeetingDao
import com.devflow.app.features.meetings.data.local.entity.MeetingEntity
import com.devflow.app.features.notes.data.local.dao.NoteDao
import com.devflow.app.features.notes.data.local.entity.NoteEntity
import com.devflow.app.features.issues.data.local.dao.IssueDao
import com.devflow.app.features.issues.data.local.entity.IssueEntity
import com.devflow.app.features.issues.domain.model.IssueStatus
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.monitoring.data.local.dao.RepositoryConfigDao
import com.devflow.app.features.monitoring.data.local.entity.RepositoryConfigEntity
import com.devflow.app.features.deadlines.data.local.dao.DeadlineDao
import com.devflow.app.features.deadlines.data.local.entity.DeadlineEntity
import com.devflow.app.features.communication.data.local.dao.MessageDao
import com.devflow.app.features.communication.data.local.entity.MessageEntity
import com.devflow.app.features.todo.data.local.dao.TodoDao
import com.devflow.app.features.todo.data.local.entity.TodoEntity
import com.devflow.app.features.todo.domain.model.TodoStatus
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProjectRepositoryImpl @Inject constructor(
    private val projectDao: ProjectDao,
    private val teamMemberDao: TeamMemberDao,
    private val sprintDao: SprintDao,
    private val taskDao: TaskDao,
    private val userStoryDao: UserStoryDao,
    private val meetingDao: MeetingDao,
    private val noteDao: NoteDao,
    private val issueDao: IssueDao,
    private val repositoryConfigDao: RepositoryConfigDao,
    private val deadlineDao: DeadlineDao,
    private val messageDao: MessageDao,
    private val todoDao: TodoDao,
    @ApplicationContext private val context: Context
) : ProjectRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("devflow_prefs", Context.MODE_PRIVATE)

    private val _activeProjectIdFlow = MutableStateFlow<Long?>(null)

    init {
        val savedId = sharedPreferences.getLong("active_project_id", -1L)
        _activeProjectIdFlow.value = if (savedId == -1L) null else savedId

        // Run async startup check to seed default demo data if database is empty
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val list = projectDao.getAll().first()
                if (list.isEmpty()) {
                    createDefaultDemoProject()
                }
            } catch (e: Exception) {
                // Fail-safe: do not crash initialization
            }
        }
    }

    override fun getAllProjects(): Flow<List<Project>> {
        return projectDao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getProject(id: Long): Project? {
        return projectDao.getById(id)?.toDomain()
    }

    override suspend fun createProject(project: Project): Long {
        require(project.name.isNotBlank()) { "Project name cannot be empty" }
        val entity = project.copy(
            status = ProjectStatus.PLANNING,
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        ).toEntity()
        val projectId = projectDao.insert(entity)

        // Populate default team members for the new project
        val defaultMembers = listOf(
            TeamMemberEntity(projectId = projectId, name = "Marcus", role = "Developer"),
            TeamMemberEntity(projectId = projectId, name = "John", role = "Backend Developer"),
            TeamMemberEntity(projectId = projectId, name = "Emily", role = "QA Engineer"),
            TeamMemberEntity(projectId = projectId, name = "David", role = "Project Manager")
        )
        defaultMembers.forEach { teamMemberDao.insert(it) }

        return projectId
    }

    override suspend fun updateProject(project: Project) {
        require(project.name.isNotBlank()) { "Project name cannot be empty" }
        val entity = project.copy(
            updatedAt = LocalDateTime.now()
        ).toEntity()
        projectDao.update(entity)
    }

    override suspend fun deleteProject(project: Project) {
        projectDao.delete(project.toEntity())
        if (_activeProjectIdFlow.value == project.id) {
            setActiveProject(null)
        }
    }

    override suspend fun archiveProject(id: Long) {
        val project = projectDao.getById(id)
        if (project != null) {
            val archived = project.copy(
                status = ProjectStatus.ARCHIVED.name,
                updatedAt = LocalDateTime.now()
            )
            projectDao.update(archived)
        }
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    override fun getActiveProject(): Flow<Project?> {
        return _activeProjectIdFlow.flatMapLatest { id ->
            if (id == null) {
                flowOf(null)
            } else {
                projectDao.getAll().map { list ->
                    list.find { it.id == id }?.toDomain()
                }
            }
        }
    }

    override suspend fun setActiveProject(projectId: Long?) {
        if (projectId == null) {
            sharedPreferences.edit().remove("active_project_id").apply()
        } else {
            sharedPreferences.edit().putLong("active_project_id", projectId).apply()
        }
        _activeProjectIdFlow.value = projectId
    }

    private suspend fun createDefaultDemoProject() {
        val projectEntity = ProjectEntity(
            name = "DevFlow Demo",
            description = "A sample software engineering workspace showcasing agile planning, developer tools integration, team chat, meeting logs, deadlines, and dashboard analytics.",
            status = ProjectStatus.ACTIVE.name,
            startDate = LocalDate.now().minusDays(15),
            endDate = LocalDate.now().plusMonths(3),
            createdAt = LocalDateTime.now().minusDays(15),
            updatedAt = LocalDateTime.now()
        )
        val projectId = projectDao.insert(projectEntity)
        
        seedMockData(projectId)
        
        setActiveProject(projectId)
    }

    private suspend fun seedMockData(projectId: Long) {
        // 1. Populate default team members for the project and store generated IDs
        val marcusId = teamMemberDao.insert(TeamMemberEntity(projectId = projectId, name = "Marcus", role = "Developer"))
        val johnId = teamMemberDao.insert(TeamMemberEntity(projectId = projectId, name = "John", role = "Backend Developer"))
        val emilyId = teamMemberDao.insert(TeamMemberEntity(projectId = projectId, name = "Emily", role = "QA Engineer"))
        val davidId = teamMemberDao.insert(TeamMemberEntity(projectId = projectId, name = "David", role = "Project Manager"))

        // 2. Populate Sprints
        val sprint1Id = sprintDao.insert(
            SprintEntity(
                projectId = projectId,
                name = "Sprint 1: Architecture Setup",
                goal = "Establish database schema layer, auto-migrations, and repository bindings.",
                startDate = LocalDate.now().minusDays(15),
                endDate = LocalDate.now().minusDays(2),
                createdAt = LocalDateTime.now().minusDays(15),
                updatedAt = LocalDateTime.now().minusDays(2)
            )
        )
        val sprint2Id = sprintDao.insert(
            SprintEntity(
                projectId = projectId,
                name = "Sprint 2: Workspace Isolation",
                goal = "Filter repositories/ViewModels by active projectId and implement custom UI switchers.",
                startDate = LocalDate.now().minusDays(1),
                endDate = LocalDate.now().plusDays(13),
                createdAt = LocalDateTime.now().minusDays(2),
                updatedAt = LocalDateTime.now()
            )
        )

        // 3. User Stories
        val story1Id = userStoryDao.insert(
            UserStoryEntity(
                projectId = projectId,
                sprintId = sprint1Id,
                title = "Database Scoping Mapping",
                description = "As a developer, I want to map all tables with foreign keys referencing projects.",
                acceptanceCriteria = "CASCADE deletes work; all child models map projectId fields properly.",
                priority = StoryPriority.CRITICAL.name,
                createdAt = LocalDateTime.now().minusDays(12),
                updatedAt = LocalDateTime.now().minusDays(10)
            )
        )
        val story2Id = userStoryDao.insert(
            UserStoryEntity(
                projectId = projectId,
                sprintId = sprint2Id,
                title = "Navigation Drawer Redesign",
                description = "As Marcus, I want workspace-isolated navigation to switch easily between projects.",
                acceptanceCriteria = "Navigation drawer shows only active project items; switcher lists all.",
                priority = StoryPriority.HIGH.name,
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now()
            )
        )
        val story3Id = userStoryDao.insert(
            UserStoryEntity(
                projectId = projectId,
                sprintId = null,
                title = "CI/CD Monitor Integration",
                description = "As a developer, I want to connect repository settings to display latest workflow status.",
                acceptanceCriteria = "Build status displays on dashboard card; details shows runner metadata.",
                priority = StoryPriority.MEDIUM.name,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        // 4. Tasks
        taskDao.insert(
            TaskEntity(
                projectId = projectId,
                title = "Add projectId foreign key constraints to entities",
                description = "Update entities and configure destructive Room migrations.",
                priority = TaskPriority.CRITICAL.name,
                status = TaskStatus.COMPLETED.name,
                dueDate = null,
                sprintId = sprint1Id,
                createdAt = LocalDateTime.now().minusDays(14),
                updatedAt = LocalDateTime.now().minusDays(12)
            )
        )
        taskDao.insert(
            TaskEntity(
                projectId = projectId,
                title = "Create ProjectListScreen Compose UI layout",
                description = "Design status badges, active indicators, and deletion icons.",
                priority = TaskPriority.HIGH.name,
                status = TaskStatus.IN_PROGRESS.name,
                dueDate = null,
                sprintId = sprint2Id,
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now()
            )
        )
        taskDao.insert(
            TaskEntity(
                projectId = projectId,
                title = "Write Unit Tests for dashboard statistics verification",
                description = "Mock repo methods and ensure counts match active project data.",
                priority = TaskPriority.MEDIUM.name,
                status = TaskStatus.PENDING.name,
                dueDate = null,
                sprintId = sprint2Id,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
        taskDao.insert(
            TaskEntity(
                projectId = projectId,
                title = "Refactor personal todo UI layout",
                description = "Enhance checklist aesthetics.",
                priority = TaskPriority.LOW.name,
                status = TaskStatus.PENDING.name,
                dueDate = null,
                sprintId = null,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        // 5. Meetings
        meetingDao.insert(
            MeetingEntity(
                projectId = projectId,
                title = "Daily Standup meeting",
                meetingDate = LocalDate.now(),
                meetingTime = "09:30 AM",
                yesterdayWork = "Finished Room schema migration and foreign keys validation.",
                todayPlan = "Build AddEditProject screen and implement dashboard widgets.",
                blockers = "None",
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
        meetingDao.insert(
            MeetingEntity(
                projectId = projectId,
                title = "Sprint Planning meeting",
                meetingDate = LocalDate.now().minusDays(2),
                meetingTime = "11:00 AM",
                yesterdayWork = "Sprint 1 review completed.",
                todayPlan = "Backlog refinement and team task assignments.",
                blockers = "QA environment credentials pending.",
                createdAt = LocalDateTime.now().minusDays(2),
                updatedAt = LocalDateTime.now().minusDays(2)
            )
        )

        // 6. Notes
        noteDao.insert(
            NoteEntity(
                projectId = projectId,
                title = "Database Migration Notes",
                content = "Room schema version updated to 7.\nCascading deletes enabled so deleting a project automatically purges child entities to maintain data integrity.",
                createdAt = LocalDateTime.now().minusDays(5),
                updatedAt = LocalDateTime.now().minusDays(5)
            )
        )
        noteDao.insert(
            NoteEntity(
                projectId = projectId,
                title = "Active Project SharedPreferences Key",
                content = "Key used: 'active_project_id'\nStored in preferences file 'devflow_prefs'.",
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now()
            )
        )

        // 7. Issues
        issueDao.insert(
            IssueEntity(
                projectId = projectId,
                title = "Null pointer exception on drawer profile select",
                description = "Profile image view throws exception if no drawable is returned from resource provider.",
                priority = IssuePriority.HIGH.name,
                status = IssueStatus.OPEN.name,
                createdAt = LocalDateTime.now().minusDays(3),
                updatedAt = LocalDateTime.now().minusDays(3)
            )
        )
        issueDao.insert(
            IssueEntity(
                projectId = projectId,
                title = "GitHub workflow status fetch fails behind proxy",
                description = "HTTP connection times out after 8000ms. Needs a proxy configurator or fallback state.",
                priority = IssuePriority.MEDIUM.name,
                status = IssueStatus.IN_PROGRESS.name,
                createdAt = LocalDateTime.now().minusDays(1),
                updatedAt = LocalDateTime.now()
            )
        )

        // 8. Repository Config (Google Gson is public and works out of the box!)
        repositoryConfigDao.insert(
            RepositoryConfigEntity(
                projectId = projectId,
                owner = "google",
                repository = "gson",
                personalAccessToken = null,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        // 9. Deadlines
        deadlineDao.insert(
            DeadlineEntity(
                projectId = projectId,
                title = "Sprint 2 Deliverable Review",
                description = "Complete task mapping tests and showcase clean UI navigation drawer.",
                dueDate = LocalDate.now().plusDays(5),
                isCompleted = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )
        deadlineDao.insert(
            DeadlineEntity(
                projectId = projectId,
                title = "Beta Release Milestones",
                description = "Publish initial application build with local DB features.",
                dueDate = LocalDate.now().plusDays(20),
                isCompleted = false,
                createdAt = LocalDateTime.now(),
                updatedAt = LocalDateTime.now()
            )
        )

        // 10. Messages (Team Chat)
        messageDao.insert(
            MessageEntity(
                senderId = johnId,
                receiverId = marcusId,
                message = "Hey Marcus, are the schema migrations finalized?",
                sentAt = LocalDateTime.now().minusHours(2)
            )
        )
        messageDao.insert(
            MessageEntity(
                senderId = marcusId,
                receiverId = johnId,
                message = "Yes, all migrated to v7 with CASCADE deletes. Give it a pull!",
                sentAt = LocalDateTime.now().minusHours(1).minusMinutes(45)
            )
        )
        messageDao.insert(
            MessageEntity(
                senderId = johnId,
                receiverId = marcusId,
                message = "Awesome! Pulling and testing now.",
                sentAt = LocalDateTime.now().minusHours(1).minusMinutes(30)
            )
        )
        messageDao.insert(
            MessageEntity(
                senderId = emilyId,
                receiverId = marcusId,
                message = "Hi Marcus, I logged a layout issue regarding profile photo in team chat.",
                sentAt = LocalDateTime.now().minusHours(1)
            )
        )

        if (todoDao.getAll().first().isEmpty()) {
            todoDao.insert(
                TodoEntity(
                    title = "Review Emily's QA report on project workspace settings",
                    description = "Analyze user feedback on drawer UX transitions.",
                    dueDate = LocalDate.now().plusDays(2),
                    status = TodoStatus.PENDING.name,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            )
            todoDao.insert(
                TodoEntity(
                    title = "Buy fresh coffee beans for the dev room",
                    description = null,
                    dueDate = null,
                    status = TodoStatus.COMPLETED.name,
                    createdAt = LocalDateTime.now().minusDays(1),
                    updatedAt = LocalDateTime.now().minusDays(1)
                )
            )
            todoDao.insert(
                TodoEntity(
                    title = "Write API integration docs for DevOps CI dashboard",
                    description = "Document configuration structure and token verification.",
                    dueDate = LocalDate.now().plusDays(7),
                    status = TodoStatus.PENDING.name,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
            )
        }
    }
}
