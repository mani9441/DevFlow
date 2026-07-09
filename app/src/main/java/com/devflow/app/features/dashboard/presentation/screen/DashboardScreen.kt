package com.devflow.app.features.dashboard.presentation.screen

import com.devflow.app.R
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
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
                title = "DevFlow",
                logoRes = R.drawable.logo,
                actions = {
                    IconButton(
                        onClick = { viewModel.refreshDashboard() },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Dashboard",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
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

            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(6.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    MaterialTheme.colorScheme.primary,
                                    MaterialTheme.colorScheme.secondary
                                )
                            )
                        )
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Good morning, Marcus",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = dateString,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.85f)
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Welcome back to your workspace. All systems are operational.",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .size(56.dp)
                            .background(MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.25f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "MC",
                            fontWeight = FontWeight.ExtraBold,
                            color = MaterialTheme.colorScheme.onPrimary,
                            style = MaterialTheme.typography.titleLarge
                        )
                    }
                }
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatBadge(
                    label = "Meetings",
                    count = state.meetings.size,
                    color = Color(0xFF0D9488),
                    modifier = Modifier.weight(1f)
                )
                StatBadge(
                    label = "Tasks",
                    count = state.todos.size,
                    color = Color(0xFF2563EB),
                    modifier = Modifier.weight(1f)
                )
                StatBadge(
                    label = "Issues",
                    count = state.issues.size,
                    color = Color(0xFFE11D48),
                    modifier = Modifier.weight(1f)
                )
                StatBadge(
                    label = "Deadlines",
                    count = state.deadlines.size,
                    color = Color(0xFFEA580C),
                    modifier = Modifier.weight(1f)
                )
            }

            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                maxItemsInEachRow = 2
            ) {
                val itemModifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()

                DashboardCard(
                    title = "Today's Meetings",
                    onClick = onNavigateToMeetings,
                    accentColor = Color(0xFF0D9488),
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

                DashboardCard(
                    title = "Upcoming Deadlines",
                    onClick = onNavigateToDeadlines,
                    accentColor = Color(0xFFEA580C),
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

                DashboardCard(
                    title = "Personal Tasks",
                    onClick = onNavigateToTodos,
                    accentColor = Color(0xFF2563EB),
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
                                        color = Color(0xFF2563EB),
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

                DashboardCard(
                    title = "Open Issues",
                    onClick = onNavigateToIssues,
                    accentColor = Color(0xFFE11D48),
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

            DashboardCard(
                title = "Latest Build Status",
                onClick = onNavigateToMonitoring,
                accentColor = Color(0xFF64748B)
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
                                    fontWeight = FontWeight.Bold,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Run #${state.latestRun.runNumber} • Branch: ${state.latestRun.branch}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            
                            val conclusionLabel: String
                            val badgeBgColor: Color
                            val badgeTextColor: Color
                            val iconVector: ImageVector
                            when (state.latestRun.conclusion) {
                                "success" -> {
                                    conclusionLabel = "Passed"
                                    badgeBgColor = Color(0xFFDCFCE7)
                                    badgeTextColor = Color(0xFF15803D)
                                    iconVector = Icons.Default.CheckCircle
                                }
                                "failure", "cancelled" -> {
                                    conclusionLabel = "Failed"
                                    badgeBgColor = Color(0xFFFEE2E2)
                                    badgeTextColor = Color(0xFFB91C1C)
                                    iconVector = Icons.Default.Close
                                }
                                else -> {
                                    conclusionLabel = "Building"
                                    badgeBgColor = Color(0xFFDBEAFE)
                                    badgeTextColor = Color(0xFF1D4ED8)
                                    iconVector = Icons.Default.Info
                                }
                            }
                            
                            Card(
                                colors = CardDefaults.cardColors(containerColor = badgeBgColor),
                                shape = RoundedCornerShape(8.dp),
                                elevation = CardDefaults.cardElevation(0.dp)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Icon(
                                        imageVector = iconVector,
                                        contentDescription = conclusionLabel,
                                        tint = badgeTextColor,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = conclusionLabel,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = badgeTextColor
                                    )
                                }
                            }
                        }
                    }
                }
            }

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column {
                    Text(
                        text = "Quick Actions",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        QuickActionTile(
                            text = "New Todo",
                            icon = Icons.Default.CheckCircle,
                            color = Color(0xFF2563EB),
                            onClick = onAddTodoClick,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionTile(
                            text = "New Note",
                            icon = Icons.Default.Edit,
                            color = Color(0xFF0D9488),
                            onClick = onAddNoteClick,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionTile(
                            text = "Meeting",
                            icon = Icons.Default.DateRange,
                            color = Color(0xFFF59E0B),
                            onClick = onAddMeetingClick,
                            modifier = Modifier.weight(1f)
                        )
                        QuickActionTile(
                            text = "Log Issue",
                            icon = Icons.Default.Warning,
                            color = Color(0xFFDC2626),
                            onClick = onAddIssueClick,
                            modifier = Modifier.weight(1f)
                        )
                    }
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
    accentColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable () -> Unit
) {
    AppCard(
        modifier = modifier.clickable { onClick() }
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .width(4.dp)
                    .height(48.dp)
                    .background(accentColor, shape = RoundedCornerShape(2.dp))
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = accentColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f))
                Spacer(modifier = Modifier.height(12.dp))
                content()
            }
        }
    }
}

@Composable
private fun StatBadge(
    label: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
private fun QuickActionTile(
    text: String,
    icon: ImageVector,
    color: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .heightIn(min = 80.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.06f)
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.15f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(color.copy(alpha = 0.12f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = color,
                    modifier = Modifier.size(18.dp)
                )
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = text,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
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