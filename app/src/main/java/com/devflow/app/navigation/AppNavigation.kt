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
                    modifier = Modifier.width(320.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                    ) {
                        // Header
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            MaterialTheme.colorScheme.primary,
                                            MaterialTheme.colorScheme.primary.copy(alpha = 0.8f)
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
                                        modifier = Modifier.size(48.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "DevFlow",
                                        style = MaterialTheme.typography.titleLarge,
                                        fontWeight = FontWeight.Black,
                                        color = MaterialTheme.colorScheme.onPrimary
                                    )
                                }
                                Spacer(modifier = Modifier.height(16.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(40.dp)
                                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.2f), CircleShape),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "MC",
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Column {
                                        Text(
                                            text = "Marcus",
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onPrimary,
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                        Text(
                                            text = "Lead Developer",
                                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f),
                                            style = MaterialTheme.typography.bodySmall
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Navigation items
                        DrawerCategoryHeader(title = "General")
                        DrawerNavItem(
                            label = "Dashboard",
                            icon = Icons.Default.Home,
                            selected = currentRoute == NavRoutes.Dashboard.route,
                            onClick = { navigateTo(NavRoutes.Dashboard.route) }
                        )

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        DrawerCategoryHeader(title = "Agile & Project Management")
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

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        DrawerCategoryHeader(title = "Collaboration")
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

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        DrawerCategoryHeader(title = "Personal Workspace")
                        DrawerNavItem(
                            label = "Personal Todos",
                            icon = Icons.Default.CheckCircle,
                            selected = currentRoute == TodoDestination.LIST,
                            onClick = { navigateTo(TodoDestination.LIST) }
                        )
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

                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp), color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))

                        DrawerCategoryHeader(title = "DevOps")
                        DrawerNavItem(
                            label = "CI/CD Builds",
                            icon = Icons.Default.Settings,
                            selected = currentRoute == MonitoringDestination.DASHBOARD,
                            onClick = { navigateTo(MonitoringDestination.DASHBOARD) }
                        )
                        
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
        style = MaterialTheme.typography.labelMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
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
                fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
            )
        },
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        selected = selected,
        onClick = onClick,
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
            selectedTextColor = MaterialTheme.colorScheme.primary,
            unselectedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unselectedContainerColor = Color.Transparent
        ),
        modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
    )
}
