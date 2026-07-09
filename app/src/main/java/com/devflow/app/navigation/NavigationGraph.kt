package com.devflow.app.navigation

import androidx.compose.runtime.Composable
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
    }
}
