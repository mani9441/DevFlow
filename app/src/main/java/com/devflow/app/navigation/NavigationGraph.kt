package com.devflow.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.NavHostController
import com.devflow.app.features.dashboard.presentation.screen.DashboardScreen
import com.devflow.app.features.notes.navigation.NotesDestination
import com.devflow.app.features.notes.presentation.screen.AddEditNoteScreen
import com.devflow.app.features.notes.presentation.screen.NoteDetailsScreen
import com.devflow.app.features.notes.presentation.screen.NotesListScreen
import com.devflow.app.features.todo.navigation.TodoDestination
import com.devflow.app.features.todo.presentation.screen.AddEditTodoScreen
import com.devflow.app.features.todo.presentation.screen.TodoDetailsScreen
import com.devflow.app.features.todo.presentation.screen.TodoListScreen
import com.devflow.app.features.meetings.navigation.MeetingsDestination
import com.devflow.app.features.meetings.presentation.screen.AddEditMeetingScreen
import com.devflow.app.features.meetings.presentation.screen.MeetingDetailsScreen
import com.devflow.app.features.meetings.presentation.screen.MeetingsListScreen
import com.devflow.app.features.deadlines.navigation.DeadlinesDestination
import com.devflow.app.features.deadlines.presentation.screen.AddEditDeadlineScreen
import com.devflow.app.features.deadlines.presentation.screen.DeadlineDetailsScreen
import com.devflow.app.features.deadlines.presentation.screen.DeadlinesListScreen
import com.devflow.app.features.sprint.navigation.SprintDestination
import com.devflow.app.features.sprint.presentation.screen.AddEditSprintScreen
import com.devflow.app.features.sprint.presentation.screen.SprintDetailsScreen
import com.devflow.app.features.sprint.presentation.screen.SprintListScreen
import com.devflow.app.features.tasks.navigation.TasksDestination
import com.devflow.app.features.tasks.presentation.screen.AddEditTaskScreen
import com.devflow.app.features.tasks.presentation.screen.TaskDetailsScreen
import com.devflow.app.features.tasks.presentation.screen.TasksListScreen
import com.devflow.app.features.stories.navigation.StoriesDestination
import com.devflow.app.features.stories.presentation.screen.AddEditStoryScreen
import com.devflow.app.features.stories.presentation.screen.StoryDetailsScreen
import com.devflow.app.features.stories.presentation.screen.StoriesListScreen
import com.devflow.app.features.communication.navigation.CommunicationDestination
import com.devflow.app.features.communication.presentation.screen.ConversationScreen
import com.devflow.app.features.communication.presentation.screen.TeamMembersListScreen
import com.devflow.app.features.issues.navigation.IssuesDestination
import com.devflow.app.features.issues.presentation.screen.AddEditIssueScreen
import com.devflow.app.features.issues.presentation.screen.IssueDetailsScreen
import com.devflow.app.features.issues.presentation.screen.IssuesListScreen
import com.devflow.app.features.monitoring.navigation.MonitoringDestination
import com.devflow.app.features.monitoring.presentation.screen.BuildDetailsScreen
import com.devflow.app.features.monitoring.presentation.screen.MonitoringDashboardScreen
import com.devflow.app.features.monitoring.presentation.viewmodel.DevelopmentMonitoringViewModel
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun NavigationGraph(
    navController: NavHostController
) {
    NavHost(
        navController = navController,
        startDestination = NavRoutes.Dashboard.route
    ) {
        composable(NavRoutes.Dashboard.route) {
            DashboardScreen(
                onNavigateToTodos = {
                    navController.navigate(TodoDestination.LIST)
                },
                onNavigateToNotes = {
                    navController.navigate(NotesDestination.LIST)
                },
                onNavigateToMeetings = {
                    navController.navigate(MeetingsDestination.LIST)
                },
                onNavigateToDeadlines = {
                    navController.navigate(DeadlinesDestination.LIST)
                },
                onNavigateToSprints = {
                    navController.navigate(SprintDestination.LIST)
                },
                onNavigateToTasks = {
                    navController.navigate(TasksDestination.LIST)
                },
                onNavigateToStories = {
                    navController.navigate(StoriesDestination.LIST)
                },
                onNavigateToCommunication = {
                    navController.navigate(CommunicationDestination.MEMBERS_LIST)
                },
                onNavigateToIssues = {
                    navController.navigate(IssuesDestination.LIST)
                },
                onNavigateToMonitoring = {
                    navController.navigate(MonitoringDestination.DASHBOARD)
                }
            )
        }

        composable(TodoDestination.LIST) {
            TodoListScreen(
                onAddTodo = {
                    navController.navigate(TodoDestination.ADD)
                },
                onTodoClick = { todoId ->
                    navController.navigate(TodoDestination.details(todoId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(TodoDestination.ADD) {
            AddEditTodoScreen(
                todoId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "todo_edit/{todoId}",
            arguments = listOf(
                navArgument("todoId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getLong("todoId")
            AddEditTodoScreen(
                todoId = todoId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = TodoDestination.DETAILS,
            arguments = listOf(
                navArgument("todoId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val todoId = backStackEntry.arguments?.getLong("todoId") ?: -1L
            TodoDetailsScreen(
                todoId = todoId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate("todo_edit/$id")
                }
            )
        }

        // Notes Module Routes
        composable(NotesDestination.LIST) {
            NotesListScreen(
                onAddNote = {
                    navController.navigate(NotesDestination.ADD)
                },
                onNoteClick = { noteId ->
                    navController.navigate(NotesDestination.details(noteId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(NotesDestination.ADD) {
            AddEditNoteScreen(
                noteId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "notes_edit/{noteId}",
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId")
            AddEditNoteScreen(
                noteId = noteId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = NotesDestination.DETAILS,
            arguments = listOf(
                navArgument("noteId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val noteId = backStackEntry.arguments?.getLong("noteId") ?: -1L
            NoteDetailsScreen(
                noteId = noteId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate("notes_edit/$id")
                }
            )
        }

        // Meetings Module Routes
        composable(MeetingsDestination.LIST) {
            MeetingsListScreen(
                onAddMeeting = {
                    navController.navigate(MeetingsDestination.ADD)
                },
                onMeetingClick = { meetingId ->
                    navController.navigate(MeetingsDestination.details(meetingId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(MeetingsDestination.ADD) {
            AddEditMeetingScreen(
                meetingId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "meetings_edit/{meetingId}",
            arguments = listOf(
                navArgument("meetingId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getLong("meetingId")
            AddEditMeetingScreen(
                meetingId = meetingId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        @Suppress("DEPRECATION")
        composable(
            route = MeetingsDestination.DETAILS,
            arguments = listOf(
                navArgument("meetingId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val meetingId = backStackEntry.arguments?.getLong("meetingId") ?: -1L
            MeetingDetailsScreen(
                meetingId = meetingId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate("meetings_edit/$id")
                }
            )
        }

        // Deadlines Module Routes
        composable(DeadlinesDestination.LIST) {
            DeadlinesListScreen(
                onAddDeadline = {
                    navController.navigate(DeadlinesDestination.ADD)
                },
                onDeadlineClick = { deadlineId ->
                    navController.navigate(DeadlinesDestination.details(deadlineId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(DeadlinesDestination.ADD) {
            AddEditDeadlineScreen(
                deadlineId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "deadlines_edit/{deadlineId}",
            arguments = listOf(
                navArgument("deadlineId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val deadlineId = backStackEntry.arguments?.getLong("deadlineId")
            AddEditDeadlineScreen(
                deadlineId = deadlineId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = DeadlinesDestination.DETAILS,
            arguments = listOf(
                navArgument("deadlineId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val deadlineId = backStackEntry.arguments?.getLong("deadlineId") ?: -1L
            DeadlineDetailsScreen(
                deadlineId = deadlineId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate("deadlines_edit/$id")
                }
            )
        }

        // Sprints Module Routes
        composable(SprintDestination.LIST) {
            SprintListScreen(
                onAddSprint = {
                    navController.navigate(SprintDestination.ADD)
                },
                onSprintClick = { sprintId ->
                    navController.navigate(SprintDestination.details(sprintId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(SprintDestination.ADD) {
            AddEditSprintScreen(
                sprintId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "sprints_edit/{sprintId}",
            arguments = listOf(
                navArgument("sprintId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val sprintId = backStackEntry.arguments?.getLong("sprintId")
            AddEditSprintScreen(
                sprintId = sprintId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = SprintDestination.DETAILS,
            arguments = listOf(
                navArgument("sprintId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val sprintId = backStackEntry.arguments?.getLong("sprintId") ?: -1L
            SprintDetailsScreen(
                sprintId = sprintId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate("sprints_edit/$id")
                },
                onTaskClick = { taskId ->
                    navController.navigate(TasksDestination.details(taskId))
                },
                onStoryClick = { storyId ->
                    navController.navigate(StoriesDestination.details(storyId))
                }
            )
        }

        // Tasks Module Routes
        composable(TasksDestination.LIST) {
            TasksListScreen(
                onAddTask = {
                    navController.navigate(TasksDestination.ADD)
                },
                onTaskClick = { taskId ->
                    navController.navigate(TasksDestination.details(taskId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(TasksDestination.ADD) {
            AddEditTaskScreen(
                taskId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "tasks_edit/{taskId}",
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId")
            AddEditTaskScreen(
                taskId = taskId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = TasksDestination.DETAILS,
            arguments = listOf(
                navArgument("taskId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val taskId = backStackEntry.arguments?.getLong("taskId") ?: -1L
            TaskDetailsScreen(
                taskId = taskId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate("tasks_edit/$id")
                }
            )
        }

        // Stories Module Routes
        composable(StoriesDestination.LIST) {
            StoriesListScreen(
                onAddStory = {
                    navController.navigate(StoriesDestination.ADD)
                },
                onStoryClick = { storyId ->
                    navController.navigate(StoriesDestination.details(storyId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(StoriesDestination.ADD) {
            AddEditStoryScreen(
                storyId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "stories_edit/{storyId}",
            arguments = listOf(
                navArgument("storyId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getLong("storyId")
            AddEditStoryScreen(
                storyId = storyId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = StoriesDestination.DETAILS,
            arguments = listOf(
                navArgument("storyId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val storyId = backStackEntry.arguments?.getLong("storyId") ?: -1L
            StoryDetailsScreen(
                storyId = storyId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate("stories_edit/$id")
                }
            )
        }

        // Communication Module Routes
        composable(CommunicationDestination.MEMBERS_LIST) {
            TeamMembersListScreen(
                onMemberClick = { memberId ->
                    navController.navigate(CommunicationDestination.conversation(memberId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(
            route = CommunicationDestination.CONVERSATION,
            arguments = listOf(
                navArgument("receiverId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val receiverId = backStackEntry.arguments?.getLong("receiverId") ?: -1L
            ConversationScreen(
                receiverId = receiverId,
                onBackClick = { navController.popBackStack() }
            )
        }

        // Issues Module Routes
        composable(IssuesDestination.LIST) {
            IssuesListScreen(
                onAddIssue = {
                    navController.navigate(IssuesDestination.ADD)
                },
                onIssueClick = { issueId ->
                    navController.navigate(IssuesDestination.details(issueId))
                },
                onBackClick = {
                    navController.popBackStack()
                }
            )
        }

        composable(IssuesDestination.ADD) {
            AddEditIssueScreen(
                issueId = null,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = "issues_edit/{issueId}",
            arguments = listOf(
                navArgument("issueId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val issueId = backStackEntry.arguments?.getLong("issueId")
            AddEditIssueScreen(
                issueId = issueId,
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable(
            route = IssuesDestination.DETAILS,
            arguments = listOf(
                navArgument("issueId") {
                    type = NavType.LongType
                }
            )
        ) { backStackEntry ->
            val issueId = backStackEntry.arguments?.getLong("issueId") ?: -1L
            IssueDetailsScreen(
                issueId = issueId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate("issues_edit/$id")
                }
            )
        }

        // Monitoring Module Routes
        composable(MonitoringDestination.DASHBOARD) { backStackEntry ->
            val viewModel: DevelopmentMonitoringViewModel = hiltViewModel(backStackEntry)
            MonitoringDashboardScreen(
                onBuildClick = {
                    navController.navigate(MonitoringDestination.DETAILS)
                },
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = viewModel
            )
        }

        composable(MonitoringDestination.DETAILS) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(MonitoringDestination.DASHBOARD)
            }
            val sharedViewModel: DevelopmentMonitoringViewModel = hiltViewModel(parentEntry)
            BuildDetailsScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                viewModel = sharedViewModel
            )
        }
    }
}
