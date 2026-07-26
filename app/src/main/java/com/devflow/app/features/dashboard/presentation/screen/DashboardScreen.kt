package com.devflow.app.features.dashboard.presentation.screen

import com.devflow.app.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.project.domain.model.ProjectStatus
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
    onAddTaskClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    onAddMeetingClick: () -> Unit,
    onAddIssueClick: () -> Unit,
    onSwitchProject: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val dateString = remember {
        val today = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.UK)
        today.format(formatter)
    }

    val activeProject = state.activeProject

    Scaffold(
        topBar = {
            AppTopBar(
                title = activeProject?.name ?: "DevFlow Workspace",
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
                .background(MaterialTheme.colorScheme.background)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(4.dp))

            // Project Header Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Brush.linearGradient(
                                colors = listOf(
                                    Color(0xFF0F172A), // Deep Slate
                                    Color(0xFF1E293B)
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
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Normal,
                            color = Color(0xFF94A3B8) // Muted blue grey
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        if (activeProject != null) {
                            Text(
                                text = activeProject.name,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Black,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            val statusColor = when (activeProject.status) {
                                ProjectStatus.PLANNING -> Color(0xFFF59E0B)
                                ProjectStatus.ACTIVE -> Color(0xFF10B981)
                                ProjectStatus.COMPLETED -> Color(0xFF3B82F6)
                                ProjectStatus.ARCHIVED -> Color(0xFF9CA3AF)
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = statusColor.copy(alpha = 0.2f),
                                contentColor = statusColor
                            ) {
                                Text(
                                    text = activeProject.status.name.lowercase().replaceFirstChar { it.uppercase() },
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        } else {
                            Text(
                                text = "Select a Project Workspace",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White,
                                modifier = Modifier.clickable { onSwitchProject() }
                            )
                        }
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = dateString,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = onSwitchProject,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = Color.White
                        ),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                    ) {
                        Text(
                            text = "Switch",
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.labelMedium
                        )
                    }
                }
            }

            if (activeProject == null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Button(
                        onClick = onSwitchProject,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("Open Project Workspace")
                    }
                }
            } else {
                // Statistics Badges
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    StatBadge(
                        label = "Sprints",
                        count = state.sprintsCount,
                        color = Color(0xFF0EA5E9),
                        modifier = Modifier.weight(1f)
                    )
                    StatBadge(
                        label = "Meetings",
                        count = state.meetings.size,
                        color = Color(0xFF14B8A6),
                        modifier = Modifier.weight(1f)
                    )
                    StatBadge(
                        label = "Notes",
                        count = state.notesCount,
                        color = Color(0xFF8B5CF6),
                        modifier = Modifier.weight(1f)
                    )
                    StatBadge(
                        label = "Issues",
                        count = state.issues.size,
                        color = Color(0xFFF43F5E),
                        modifier = Modifier.weight(1f)
                    )
                }

                // Grid cards
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    maxItemsInEachRow = 2
                ) {
                    val itemModifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()

                    // Current Sprint Card
                    DashboardCard(
                        title = "Current Sprint",
                        onClick = onNavigateToSprints,
                        accentColor = Color(0xFF0EA5E9),
                        modifier = itemModifier
                    ) {
                        val sprint = state.currentSprint
                        if (sprint == null) {
                            EmptyStateText(text = "No active sprint.")
                        } else {
                            Column {
                                Text(
                                    text = sprint.name,
                                    style = MaterialTheme.typography.bodyLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = sprint.goal,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.DateRange,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.outline,
                                        modifier = Modifier.size(14.dp)
                                    )
                                    Text(
                                        text = "Ends: ${sprint.endDate.format(DateTimeFormatter.ofPattern("d MMM yyyy", Locale.UK))}",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.outline,
                                        fontWeight = FontWeight.Medium
                                    )
                                }
                            }
                        }
                    }

                    // Today's Meetings Card
                    DashboardCard(
                        title = "Today's Meetings",
                        onClick = onNavigateToMeetings,
                        accentColor = Color(0xFF14B8A6),
                        modifier = itemModifier
                    ) {
                        if (state.meetings.isEmpty()) {
                            EmptyStateText(text = "No meetings scheduled.")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                state.meetings.forEach { meeting ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = meeting.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = Color(0xFFF1F5F9),
                                            contentColor = Color(0xFF475569)
                                        ) {
                                            Text(
                                                text = meeting.meetingTime,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Recent Notes Card
                    DashboardCard(
                        title = "Recent Notes",
                        onClick = onNavigateToNotes,
                        accentColor = Color(0xFF8B5CF6),
                        modifier = itemModifier
                    ) {
                        if (state.recentNotes.isEmpty()) {
                            EmptyStateText(text = "No notes created.")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                state.recentNotes.forEach { note ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Edit,
                                            contentDescription = null,
                                            tint = Color(0xFF8B5CF6).copy(alpha = 0.7f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = note.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.SemiBold,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Open Issues Card
                    DashboardCard(
                        title = "Open Issues",
                        onClick = onNavigateToIssues,
                        accentColor = Color(0xFFF43F5E),
                        modifier = itemModifier
                    ) {
                        if (state.issues.isEmpty()) {
                            EmptyStateText(text = "No pending issues.")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                                state.issues.take(2).forEach { issue ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = issue.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis,
                                            modifier = Modifier.weight(1f)
                                        )
                                        val badgeColor = when (issue.priority) {
                                            IssuePriority.CRITICAL -> Color(0xFFFDA4AF)
                                            IssuePriority.HIGH -> Color(0xFFFDBA74)
                                            IssuePriority.MEDIUM -> Color(0xFFFDE047)
                                            IssuePriority.LOW -> Color(0xFFCBD5E1)
                                            else -> Color.Gray
                                        }
                                        val textCol = when (issue.priority) {
                                            IssuePriority.CRITICAL -> Color(0xFF9F1239)
                                            IssuePriority.HIGH -> Color(0xFF9A3412)
                                            IssuePriority.MEDIUM -> Color(0xFF854D0E)
                                            IssuePriority.LOW -> Color(0xFF334155)
                                            else -> Color.Black
                                        }
                                        Surface(
                                            shape = RoundedCornerShape(6.dp),
                                            color = badgeColor.copy(alpha = 0.3f),
                                            contentColor = textCol
                                        ) {
                                            Text(
                                                text = issue.priority.name,
                                                style = MaterialTheme.typography.labelSmall,
                                                fontWeight = FontWeight.ExtraBold,
                                                modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // GitHub Build Status Card
                DashboardCard(
                    title = "CI/CD Pipeline Status",
                    onClick = onNavigateToMonitoring,
                    accentColor = Color(0xFF64748B)
                ) {
                    when {
                        state.repoConfig == null -> {
                            EmptyStateText(text = "Connect GitHub repository in DevOps settings.")
                        }
                        state.latestRun == null -> {
                            EmptyStateText(text = "No builds found for: ${state.repoConfig.owner}/${state.repoConfig.repository}")
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
                                        badgeBgColor = Color(0xFFD1FAE5)
                                        badgeTextColor = Color(0xFF065F46)
                                        iconVector = Icons.Default.CheckCircle
                                    }
                                    "failure", "cancelled" -> {
                                        conclusionLabel = "Failed"
                                        badgeBgColor = Color(0xFFFEE2E2)
                                        badgeTextColor = Color(0xFF991B1B)
                                        iconVector = Icons.Default.Close
                                    }
                                    else -> {
                                        conclusionLabel = "Running"
                                        badgeBgColor = Color(0xFFDBEAFE)
                                        badgeTextColor = Color(0xFF1E40AF)
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

                // Quick Actions Card
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
                                text = "New Task",
                                icon = Icons.Default.CheckCircle,
                                color = Color(0xFF0EA5E9),
                                onClick = onAddTaskClick,
                                modifier = Modifier.weight(1f)
                            )
                            QuickActionTile(
                                text = "New Note",
                                icon = Icons.Default.Edit,
                                color = Color(0xFF8B5CF6),
                                onClick = onAddNoteClick,
                                modifier = Modifier.weight(1f)
                            )
                            QuickActionTile(
                                text = "Meeting",
                                icon = Icons.Default.DateRange,
                                color = Color(0xFF14B8A6),
                                onClick = onAddMeetingClick,
                                modifier = Modifier.weight(1f)
                            )
                            QuickActionTile(
                                text = "Log Issue",
                                icon = Icons.Default.Warning,
                                color = Color(0xFFF43F5E),
                                onClick = onAddIssueClick,
                                modifier = Modifier.weight(1f)
                            )
                        }
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
                    .height(36.dp)
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
                HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))
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
            containerColor = color.copy(alpha = 0.06f)
        ),
        shape = RoundedCornerShape(14.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, color.copy(alpha = 0.15f))
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = count.toString(),
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Black,
                color = color
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold,
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f),
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
            containerColor = Color.White
        ),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(1.dp)
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
                    .background(color.copy(alpha = 0.1f), CircleShape),
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
private fun EmptyStateText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
    )
}