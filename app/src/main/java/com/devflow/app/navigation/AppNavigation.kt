package com.devflow.app.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.app.R
import com.devflow.app.features.todo.navigation.TodoDestination
import com.devflow.app.features.notes.navigation.NotesDestination
import com.devflow.app.features.meetings.navigation.MeetingsDestination
import com.devflow.app.features.deadlines.navigation.DeadlinesDestination
import com.devflow.app.features.sprint.navigation.SprintDestination
import com.devflow.app.features.tasks.navigation.TasksDestination
import com.devflow.app.features.stories.navigation.StoriesDestination
import com.devflow.app.features.communication.navigation.CommunicationDestination
import com.devflow.app.features.issues.navigation.IssuesDestination
import com.devflow.app.features.monitoring.navigation.MonitoringDestination
import com.devflow.app.features.project.presentation.viewmodel.ProjectViewModel
import kotlinx.coroutines.launch

val LocalOpenDrawer = staticCompositionLocalOf<() -> Unit> {
    { }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    
    // Track current route to highlight active drawer items
    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry.value?.destination?.route

    // Retrieve active project details from ProjectViewModel
    val projectViewModel: ProjectViewModel = hiltViewModel()
    val projectState = projectViewModel.uiState.collectAsState().value
    val activeProject = projectState.activeProject

    val currentRouteStr = currentRoute ?: ""
    val isIndependentRoute = currentRouteStr == "workspace_dashboard" ||
                             currentRouteStr == "project_list" || 
                             currentRouteStr == "project_add" || 
                             currentRouteStr.startsWith("project_edit") ||
                             currentRouteStr == "todo_list" || 
                             currentRouteStr == "todo_add" || 
                             currentRouteStr.startsWith("todo_edit") ||
                             currentRouteStr.startsWith("todo_details")

    val showProjectItems = activeProject != null && !isIndependentRoute

    val openDrawer = {
        scope.launch { drawerState.open() }
    }

    val navigateTo = { route: String ->
        scope.launch { drawerState.close() }
        if (currentRoute != route) {
            navController.navigate(route) {
                popUpTo(navController.graph.startDestinationId) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }
        }
    }

    CompositionLocalProvider(LocalOpenDrawer provides { openDrawer() }) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = MaterialTheme.colorScheme.background,
                    modifier = Modifier.width(300.dp),
                    drawerShape = RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Header Slate Gradient
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            Color(0xFF0F172A),
                                            Color(0xFF1E293B)
                                        )
                                    )
                                )
                                .padding(24.dp)
                        ) {
                            Column {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    androidx.compose.foundation.Image(
                                        painter = painterResource(id = R.drawable.logo),
                                        contentDescription = null,
                                        modifier = Modifier.size(44.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "DevFlow",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Black,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                 if (showProjectItems) {
                                     Text(
                                         text = activeProject!!.name,
                                         style = MaterialTheme.typography.bodyLarge,
                                         fontWeight = FontWeight.Bold,
                                         color = Color.White,
                                         maxLines = 1,
                                         overflow = TextOverflow.Ellipsis
                                     )
                                     Text(
                                         text = "Workspace: ${activeProject.status.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                         style = MaterialTheme.typography.bodySmall,
                                         color = Color(0xFF94A3B8)
                                     )
                                     Spacer(modifier = Modifier.height(14.dp))
                                 }
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .background(Color.White.copy(alpha = 0.15f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "MC",
                                            fontWeight = FontWeight.ExtraBold,
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Marcus",
                                            fontWeight = FontWeight.Bold,
                                            color = Color.White,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                        Text(
                                            text = "Lead Developer",
                                            color = Color(0xFF64748B),
                                            style = MaterialTheme.typography.labelSmall
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Navigation items
                        DrawerCategoryHeader(title = "WORKSPACES")
                        DrawerNavItem(
                            label = "Workspace Dashboard",
                            icon = Icons.Default.Home,
                            selected = currentRoute == NavRoutes.WorkspaceDashboard.route,
                            onClick = { navigateTo(NavRoutes.WorkspaceDashboard.route) }
                        )
                        DrawerNavItem(
                            label = "All Projects",
                            icon = Icons.Default.Star,
                            selected = currentRoute == NavRoutes.Projects.route,
                            onClick = { navigateTo(NavRoutes.Projects.route) }
                        )

                        if (showProjectItems) {
                            DrawerNavItem(
                                label = "Project Dashboard",
                                icon = Icons.Default.Home,
                                selected = currentRoute == NavRoutes.Dashboard.route,
                                onClick = { navigateTo(NavRoutes.Dashboard.route) }
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

                            DrawerCategoryHeader(title = "AGILE PLANNING")
                            DrawerNavItem(
                                label = "Sprints",
                                icon = Icons.Default.Refresh,
                                selected = currentRoute == SprintDestination.LIST,
                                onClick = { navigateTo(SprintDestination.LIST) }
                            )
                            DrawerNavItem(
                                label = "Development Tasks",
                                icon = Icons.AutoMirrored.Filled.List,
                                selected = currentRoute == TasksDestination.LIST,
                                onClick = { navigateTo(TasksDestination.LIST) }
                            )
                            DrawerNavItem(
                                label = "User Stories",
                                icon = Icons.Default.Star,
                                selected = currentRoute == StoriesDestination.LIST,
                                onClick = { navigateTo(StoriesDestination.LIST) }
                            )
                            DrawerNavItem(
                                label = "Issue Tracker",
                                icon = Icons.Default.Warning,
                                selected = currentRoute == IssuesDestination.LIST,
                                onClick = { navigateTo(IssuesDestination.LIST) }
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

                            DrawerCategoryHeader(title = "COLLABORATION")
                            DrawerNavItem(
                                label = "Team Chat",
                                icon = Icons.Default.AccountCircle,
                                selected = currentRoute == CommunicationDestination.MEMBERS_LIST,
                                onClick = { navigateTo(CommunicationDestination.MEMBERS_LIST) }
                            )
                            DrawerNavItem(
                                label = "Meetings",
                                icon = Icons.Default.DateRange,
                                selected = currentRoute == MeetingsDestination.LIST,
                                onClick = { navigateTo(MeetingsDestination.LIST) }
                            )
                        }

                        HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

                        DrawerCategoryHeader(title = "PERSONAL")
                        DrawerNavItem(
                            label = "Personal Todos",
                            icon = Icons.Default.CheckCircle,
                            selected = currentRoute == TodoDestination.LIST,
                            onClick = { navigateTo(TodoDestination.LIST) }
                        )

                        if (showProjectItems) {
                            DrawerNavItem(
                                label = "Project Notes",
                                icon = Icons.Default.Edit,
                                selected = currentRoute == NotesDestination.LIST,
                                onClick = { navigateTo(NotesDestination.LIST) }
                            )
                            DrawerNavItem(
                                label = "Deadlines",
                                icon = Icons.Default.Notifications,
                                selected = currentRoute == DeadlinesDestination.LIST,
                                onClick = { navigateTo(DeadlinesDestination.LIST) }
                            )

                            HorizontalDivider(modifier = Modifier.padding(vertical = 10.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outline.copy(alpha = 0.4f))

                            DrawerCategoryHeader(title = "DEVOPS")
                            DrawerNavItem(
                                label = "CI/CD Builds",
                                icon = Icons.Default.Settings,
                                selected = currentRoute == MonitoringDestination.DASHBOARD,
                                onClick = { navigateTo(MonitoringDestination.DASHBOARD) }
                            )
                        }
                        
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }
            }
        ) {
            NavigationGraph(
                navController = navController
            )
        }
    }
}

@Composable
private fun DrawerCategoryHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.labelSmall,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 6.dp)
    )
}

@Composable
private fun DrawerNavItem(
    label: String,
    icon: ImageVector,
    selected: Boolean,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        label = {
            Text(
                text = label,
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                style = MaterialTheme.typography.bodyMedium
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                modifier = Modifier.size(20.dp)
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.06f),
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    )
}
