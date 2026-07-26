package com.devflow.app.features.dashboard.presentation.screen

import com.devflow.app.R
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
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
import com.devflow.app.features.dashboard.presentation.viewmodel.WorkspaceDashboardViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun WorkspaceDashboardScreen(
    onNavigateToTodos: () -> Unit,
    onNavigateToProjects: () -> Unit,
    onNavigateToProjectDashboard: () -> Unit,
    onAddProjectClick: () -> Unit,
    onAddTodoClick: () -> Unit,
    onAddMeetingClick: () -> Unit,
    onAddNoteClick: () -> Unit,
    viewModel: WorkspaceDashboardViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    val today = remember { LocalDate.now() }
    val dateString = remember {
        today.format(DateTimeFormatter.ofPattern("EEEE, d MMMM yyyy", Locale.UK))
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Workspace Dashboard",
                logoRes = R.drawable.logo,
                actions = {
                    IconButton(
                        onClick = { viewModel.refreshDashboard() },
                        modifier = Modifier.padding(end = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh Workspace",
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

            // Workspace Header Welcome
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
                                    Color(0xFF0F172A), // Slate 900
                                    Color(0xFF1E293B)
                                )
                            )
                        )
                        .padding(24.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = dateString,
                            style = MaterialTheme.typography.titleSmall,
                            color = Color(0xFF94A3B8),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "My Workspace",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Aggregated view of today's items and builds across all active projects.",
                            style = MaterialTheme.typography.bodySmall,
                            color = Color(0xFF64748B)
                        )
                    }
                }
            }

            // Section 1: Today's Focus
            CardSectionHeader(title = "Today's Focus")

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    // Meetings Today
                    FocusCategoryItem(
                        label = "Meetings Today",
                        count = state.todayMeetings.size
                    ) {
                        if (state.todayMeetings.isEmpty()) {
                            EmptyStateSubtext("No meetings scheduled for today.")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                state.todayMeetings.forEach { meeting ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = meeting.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${meeting.projectName} • ${meeting.meetingTime}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.outline
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    // Deadlines Today/Tomorrow
                    FocusCategoryItem(
                        label = "Deadlines Today / Tomorrow",
                        count = state.todayDeadlines.size
                    ) {
                        if (state.todayDeadlines.isEmpty()) {
                            EmptyStateSubtext("No upcoming deadlines for today or tomorrow.")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                state.todayDeadlines.forEach { deadline ->
                                    val isDueToday = deadline.dueDate == today
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = deadline.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${deadline.projectName} • ${if (isDueToday) "Due Today" else "Due Tomorrow"}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = if (isDueToday) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    // Personal Todos
                    FocusCategoryItem(
                        label = "Personal Tasks",
                        count = state.todayTodos.size
                    ) {
                        if (state.todayTodos.isEmpty()) {
                            EmptyStateSubtext("All personal tasks are completed!")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                state.todayTodos.take(3).forEach { todo ->
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth().clickable { onNavigateToTodos() }
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Text(
                                            text = todo.title,
                                            style = MaterialTheme.typography.bodyMedium,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }
                                if (state.todayTodos.size > 3) {
                                    Text(
                                        text = "+ ${state.todayTodos.size - 3} more personal tasks",
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.clickable { onNavigateToTodos() }
                                    )
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    // High Priority Issues
                    FocusCategoryItem(
                        label = "High Priority Issues",
                        count = state.highPriorityIssues.size
                    ) {
                        if (state.highPriorityIssues.isEmpty()) {
                            EmptyStateSubtext("No pending critical or high priority issues.")
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                state.highPriorityIssues.forEach { issue ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = issue.title,
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "${issue.projectName} • Priority: ${issue.priority.name}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    HorizontalDivider(color = MaterialTheme.colorScheme.outline.copy(alpha = 0.3f))

                    // Build Alerts
                    FocusCategoryItem(
                        label = "Build Alerts",
                        count = state.failedBuilds.size
                    ) {
                        if (state.failedBuilds.isEmpty()) {
                            val passes = state.latestBuilds.count { it.conclusion == "success" }
                            if (passes > 0) {
                                SuccessBuildAlertText("All builds passing successfully! ($passes passing)")
                            } else {
                                EmptyStateSubtext("No builds configured or running.")
                            }
                        } else {
                            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                                state.failedBuilds.forEach { build ->
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${build.projectName} - ${build.workflowName}",
                                                style = MaterialTheme.typography.bodyMedium,
                                                fontWeight = FontWeight.Bold
                                            )
                                            Text(
                                                text = "Build #${build.runNumber} Failed on branch ${build.branch}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.error
                                            )
                                        }
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Failed",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Section 2: Projects Overview
            CardSectionHeader(title = "Projects Overview")

            if (state.activeProjects.isEmpty()) {
                AppCard(modifier = Modifier.fillMaxWidth()) {
                    Box(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(onClick = onAddProjectClick) {
                            Text("Create your first Project")
                        }
                    }
                }
            } else {
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    state.activeProjects.forEach { overview ->
                        val project = overview.project
                        AppCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectProject(project) {
                                        onNavigateToProjectDashboard()
                                    }
                                }
                        ) {
                            Column(modifier = Modifier.fillMaxWidth()) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = project.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Black
                                    )
                                    Icon(
                                        imageVector = Icons.Default.PlayArrow,
                                        contentDescription = "Open Project",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                if (overview.activeSprintName != null) {
                                    Text(
                                        text = overview.activeSprintName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        LinearProgressIndicator(
                                            progress = { overview.activeSprintProgress.toFloat() / 100f },
                                            modifier = Modifier.weight(1f).height(6.dp),
                                            color = MaterialTheme.colorScheme.primary,
                                            trackColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                                            strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                                        )
                                        Spacer(modifier = Modifier.width(10.dp))
                                        Text(
                                            text = "${overview.activeSprintProgress}%",
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                } else {
                                    Text(
                                        text = "No active sprint • Lifecycle: ${project.status.name.lowercase().replaceFirstChar { it.uppercase() }}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Row for Recent Activity & Build Overview (Side-by-side or stacked)
            CardSectionHeader(title = "Recent Activity")

            AppCard(modifier = Modifier.fillMaxWidth()) {
                if (state.recentActivity.isEmpty()) {
                    EmptyStateSubtext("No recent activities.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        state.recentActivity.forEach { activity ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .background(MaterialTheme.colorScheme.secondary, CircleShape)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = "${activity.title} in ${activity.projectName}",
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = activity.description,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.8f)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Builds Overview
            CardSectionHeader(title = "Latest Builds")

            AppCard(modifier = Modifier.fillMaxWidth()) {
                if (state.latestBuilds.isEmpty()) {
                    EmptyStateSubtext("No builds configured.")
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        state.latestBuilds.forEach { build ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = build.projectName,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Text(
                                        text = "${build.workflowName} • #${build.runNumber}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                }
                                val statusText: String
                                val badgeBg: Color
                                val badgeTextCol: Color
                                when (build.conclusion) {
                                    "success" -> {
                                        statusText = "Success"
                                        badgeBg = Color(0xFFD1FAE5)
                                        badgeTextCol = Color(0xFF065F46)
                                    }
                                    "failure", "cancelled" -> {
                                        statusText = "Failed"
                                        badgeBg = Color(0xFFFEE2E2)
                                        badgeTextCol = Color(0xFF991B1B)
                                    }
                                    else -> {
                                        statusText = "Running"
                                        badgeBg = Color(0xFFDBEAFE)
                                        badgeTextCol = Color(0xFF1E40AF)
                                    }
                                }
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = badgeBg),
                                    shape = RoundedCornerShape(6.dp),
                                    elevation = CardDefaults.cardElevation(0.dp)
                                ) {
                                    Text(
                                        text = statusText,
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = badgeTextCol,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Actions
            CardSectionHeader(title = "Quick Actions")

            AppCard(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    QuickActionTile(
                        text = "New Project",
                        icon = Icons.Default.Add,
                        color = Color(0xFF0EA5E9),
                        onClick = onAddProjectClick,
                        modifier = Modifier.weight(1f)
                    )
                    QuickActionTile(
                        text = "Todo",
                        icon = Icons.Default.CheckCircle,
                        color = Color(0xFF10B981),
                        onClick = onAddTodoClick,
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
                        text = "New Note",
                        icon = Icons.Default.Edit,
                        color = Color(0xFF8B5CF6),
                        onClick = onAddNoteClick,
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CardSectionHeader(title: String) {
    Text(
        text = title,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Black,
        color = MaterialTheme.colorScheme.primary,
        modifier = Modifier.padding(start = 4.dp, top = 4.dp)
    )
}

@Composable
private fun FocusCategoryItem(
    label: String,
    count: Int,
    content: @Composable () -> Unit
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Black,
                color = MaterialTheme.colorScheme.onSurface
            )
            Surface(
                shape = RoundedCornerShape(10.dp),
                color = if (count > 0) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color(0xFFF1F5F9),
                contentColor = if (count > 0) MaterialTheme.colorScheme.primary else Color(0xFF64748B)
            ) {
                Text(
                    text = count.toString(),
                    style = MaterialTheme.typography.labelSmall,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        content()
    }
}

@Composable
private fun EmptyStateSubtext(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.5f),
        fontWeight = FontWeight.Normal
    )
}

@Composable
private fun SuccessBuildAlertText(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall,
        color = Color(0xFF059669),
        fontWeight = FontWeight.Medium
    )
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
            .heightIn(min = 72.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.5f)),
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .background(color.copy(alpha = 0.1f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = text,
                    tint = color,
                    modifier = Modifier.size(16.dp)
                )
            }
            Spacer(modifier = Modifier.height(6.dp))
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
