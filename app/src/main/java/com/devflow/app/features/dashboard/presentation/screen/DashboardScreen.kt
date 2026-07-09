package com.devflow.app.features.dashboard.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.devflow.app.core.designsystem.components.AppCard
import com.devflow.app.core.designsystem.components.AppTopBar

@Composable
fun DashboardScreen(
    onNavigateToTodos: () -> Unit,
    onNavigateToNotes: () -> Unit,
    onNavigateToMeetings: () -> Unit,
    onNavigateToDeadlines: () -> Unit,
    onNavigateToSprints: () -> Unit,
    onNavigateToTasks: () -> Unit,
    onNavigateToStories: () -> Unit,
    onNavigateToCommunication: () -> Unit,
    onNavigateToIssues: () -> Unit,
    onNavigateToMonitoring: () -> Unit
) {
    val modules = listOf(
        DashboardModuleItem(
            title = "Personal Todo",
            description = "Manage tasks, deadlines, and project todo lists in one place.",
            icon = Icons.Default.List,
            onClick = onNavigateToTodos
        ),
        DashboardModuleItem(
            title = "Project Notes",
            description = "Store and manage project-related notes, meeting outcomes, and reminders.",
            icon = Icons.Default.Edit,
            onClick = onNavigateToNotes
        ),
        DashboardModuleItem(
            title = "Stand-up Meetings",
            description = "Plan and log your daily stand-ups and developer routines.",
            icon = Icons.Default.DateRange,
            onClick = onNavigateToMeetings
        ),
        DashboardModuleItem(
            title = "Upcoming Deadlines",
            description = "Track critical project milestones and dates.",
            icon = Icons.Default.Warning,
            onClick = onNavigateToDeadlines
        ),
        DashboardModuleItem(
            title = "Sprints",
            description = "Manage agile sprints, iteration goals, and schedules.",
            icon = Icons.Default.Refresh,
            onClick = onNavigateToSprints
        ),
        DashboardModuleItem(
            title = "Development Tasks",
            description = "Track software implementation tasks and status.",
            icon = Icons.Default.List,
            onClick = onNavigateToTasks
        ),
        DashboardModuleItem(
            title = "User Stories",
            description = "Write and organize user requirements and stories.",
            icon = Icons.Default.Star,
            onClick = onNavigateToStories
        ),
        DashboardModuleItem(
            title = "Team Communication",
            description = "Simulate and track messaging thread with development team members.",
            icon = Icons.Default.AccountCircle,
            onClick = onNavigateToCommunication
        ),
        DashboardModuleItem(
            title = "Issue Tracking",
            description = "Log and manage project bugs, enhancements, and defects.",
            icon = Icons.Default.Info,
            onClick = onNavigateToIssues
        ),
        DashboardModuleItem(
            title = "Development Monitoring",
            description = "Monitor remote GitHub Actions workflows and build execution health.",
            icon = Icons.Default.Build,
            onClick = onNavigateToMonitoring
        )
    )

    Scaffold(
        topBar = {
            AppTopBar(title = "DevFlow Dashboard")
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text(
                text = "Welcome to DevFlow",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Select a module to manage your development workflow.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                modifier = Modifier.fillMaxSize()
            ) {
                items(modules) { item ->
                    AppCard(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp)
                            .clickable { item.onClick() }
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(40.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = item.title,
                                    style = MaterialTheme.typography.titleLarge
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.description,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

private data class DashboardModuleItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit
)
