package com.devflow.app.features.dashboard.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.app.core.designsystem.components.AppButton
import com.devflow.app.core.designsystem.components.AppCard
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.features.dashboard.presentation.viewmodel.DashboardViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@OptIn(ExperimentalLayoutApi::class)
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
    onNavigateToMonitoring: () -> Unit,
    onAddTodoClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onAddMeetingClick: () -> Unit,
    onAddIssueClick: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val dateString = remember {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.UK)
        today.format(formatter)
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Dashboard",
                actions = {
                    Box(
                        modifier = Modifier
                            .clickable { viewModel.refreshDashboard() }
                            .padding(8.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Dashboard",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // 1. Unified Welcome & Summary Header
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            ) {
                Column(modifier = Modifier.padding(24.dp)) {
                    Text(
                        text = "Good morning, Marcus",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = dateString,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }
            }

            // 2. Continuous Dynamic Metric Sections (Adaptive Grid Arrangement)
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 2
            ) {
                val itemModifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()

                // Today's Meetings
                DashboardCard(
                    title = "Today's Meetings",
                    onClick = onNavigateToMeetings,
                    modifier = itemModifier
                ) {
                    if (state.meetings.isEmpty()) {
                        EmptyStateText(text = "No meetings scheduled for today.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            state.meetings.forEach { meeting ->
                                MeetingRow(title = meeting.title, time = meeting.meetingTime)
                            }
                        }
                    }
                }

                // Upcoming Deadlines
                DashboardCard(
                    title = "Upcoming Deadlines",
                    onClick = onNavigateToDeadlines,
                    modifier = itemModifier
                ) {
                    if (state.deadlines.isEmpty()) {
                        EmptyStateText(text = "No upcoming deadlines.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            state.deadlines.take(2).forEach { deadline ->
                                val daysText = remember(deadline.dueDate) {
                                    val today = LocalDate.now()
                                    when {
                                        deadline.dueDate.isEqual(today) -> "Today"
                                        deadline.dueDate.isEqual(today.plusDays(1)) -> "Tomorrow"
                                        else -> deadline.dueDate.format(DateTimeFormatter.ofPattern("d MMM", Locale.UK))
                                    }
                                }
                                DeadlineRow(title = deadline.title, dueText = daysText)
                            }
                        }
                    }
                }

                // Personal Tasks
                DashboardCard(
                    title = "Personal Tasks",
                    onClick = onNavigateToTodos,
                    modifier = itemModifier
                ) {
                    if (state.todos.isEmpty()) {
                        EmptyStateText(text = "No pending personal tasks.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            state.todos.take(3).forEach { todo ->
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text(
                                        text = "•",
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.padding(end = 8.dp)
                                    )
                                    Text(
                                        text = todo.title,
                                        style = MaterialTheme.typography.bodyMedium,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }
                    }
                }

                // Open Issues Tracking
                DashboardCard(
                    title = "Open Issues",
                    onClick = onNavigateToIssues,
                    modifier = itemModifier
                ) {
                    if (state.issues.isEmpty()) {
                        EmptyStateText(text = "No open defects or issues.")
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            state.issues.take(2).forEach { issue ->
                                IssueRow(title = issue.title, status = issue.status.name.replace("_", " "))
                            }
                            if (state.issues.size > 2) {
                                Text(
                                    text = "+ ${state.issues.size - 2} more issues",
                                    style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.outline,
                                    modifier = Modifier.padding(top = 2.dp)
                                )
                            }
                        }
                    }
                }
            }

            // 3. Pipeline Run Status
            DashboardCard(
                title = "Latest Build Status",
                onClick = onNavigateToMonitoring
            ) {
                when {
                    state.repoConfig == null -> {
                        EmptyStateText(text = "GitHub repository not connected.")
                    }
                    state.latestRun == null -> {
                        EmptyStateText(text = "No build status parsed for repository: ${state.repoConfig.owner}/${state.repoConfig.repository}")
                    }
                    else -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = state.latestRun.workflowName,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Run #${state.latestRun.runNumber} • Branch: ${state.latestRun.branch}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            val (iconVector, tintColor, desc) = when (state.latestRun.conclusion) {
                                "success" -> Triple(Icons.Default.CheckCircle, androidx.compose.ui.graphics.Color(0xFF2E7D32), "Success")
                                "failure", "cancelled" -> Triple(Icons.Default.Close, MaterialTheme.colorScheme.error, "Failed")
                                else -> Triple(Icons.Default.Info, MaterialTheme.colorScheme.primary, "In Progress")
                            }
                            Icon(
                                imageVector = iconVector,
                                contentDescription = desc,
                                tint = tintColor,
                                modifier = Modifier.size(24.dp)
                            )
                        }
                    }
                }
            }

            // 4. Action Command Bar
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        AppButton(text = "New Todo", onClick = onAddTodoClick)
                        AppButton(text = "New Note", onClick = onAddNoteClick)
                        AppButton(text = "Schedule Meeting", onClick = onAddMeetingClick)
                        AppButton(text = "Log Issue", onClick = onAddIssueClick)
                    }
                }
            }

            // 5. System Workspaces Functional Navigation
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Workspace Navigation",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    Spacer(modifier = Modifier.height(8.dp))

                    WorkspaceItem(
                        title = "Agile Sprints",
                        description = "Manage agile sprints and planning iterations.",
                        icon = Icons.Default.Refresh,
                        onClick = onNavigateToSprints
                    )
                    WorkspaceItem(
                        title = "Development Tasks",
                        description = "Track software implementation tasks and boards.",
                        icon = Icons.Default.List,
                        onClick = onNavigateToTasks
                    )
                    WorkspaceItem(
                        title = "User Stories",
                        description = "Write user requirements and user scenarios.",
                        icon = Icons.Default.Star,
                        onClick = onNavigateToStories
                    )
                    WorkspaceItem(
                        title = "Project Notes",
                        description = "Keep track of notes, outcomes, and logs.",
                        icon = Icons.Default.Edit,
                        onClick = onNavigateToNotes
                    )
                    WorkspaceItem(
                        title = "Team Chat",
                        description = "Chat with team members in local threads.",
                        icon = Icons.Default.AccountCircle,
                        onClick = onNavigateToCommunication
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun DashboardCard(
    title: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    AppCard(
        modifier = modifier.clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Spacer(modifier = Modifier.height(8.dp))
            HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
            Spacer(modifier = Modifier.height(12.dp))
            content()
        }
    }
}

@Composable
private fun MeetingRow(title: String, time: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = time,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.primary,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun DeadlineRow(title: String, dueText: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Medium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = dueText,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun IssueRow(title: String, status: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.weight(1f)
        )
        Text(
            text = status,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

@Composable
private fun EmptyStateText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )
}

@Composable
fun WorkspaceItem(
    title: String,
    description: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        // Changed to Alignment.Top so elements align from the first line down
        verticalAlignment = Alignment.Top 
    ) {
        Icon(
            imageVector = icon,
            contentDescription = title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .size(20.dp)
                // Small top offset aligns the icon perfectly with the baseline of the title text
                .padding(top = 2.dp) 
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }
}

@Composable
fun Box(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.Box(modifier = modifier) {
        content()
    }
}