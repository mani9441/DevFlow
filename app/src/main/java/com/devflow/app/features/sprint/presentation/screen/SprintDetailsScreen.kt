package com.devflow.app.features.sprint.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.app.core.designsystem.components.AppButton
import com.devflow.app.core.designsystem.components.AppCard
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.core.designsystem.components.EmptyState
import com.devflow.app.core.designsystem.components.LoadingView
import com.devflow.app.features.progress.presentation.viewmodel.SprintProgressViewModel
import com.devflow.app.features.sprint.presentation.viewmodel.SprintViewModel
import java.time.format.DateTimeFormatter

private val DateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

@Composable
fun SprintDetailsScreen(
    sprintId: Long,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    onTaskClick: (Long) -> Unit,
    onStoryClick: (Long) -> Unit,
    viewModel: SprintViewModel = hiltViewModel(),
    progressViewModel: SprintProgressViewModel = hiltViewModel()
) {
    LaunchedEffect(sprintId) {
        viewModel.loadSprint(sprintId)
        progressViewModel.loadProgress(sprintId)
    }

    val state = viewModel.uiState.collectAsState().value
    val progressState = progressViewModel.progress.collectAsState().value
    val sprint = state.selectedSprint

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Sprint Details",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        when {
            state.loadingState -> {
                LoadingView()
            }
            sprint == null -> {
                EmptyState(message = "Sprint not found.")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState())
                    ) {
                        Text(
                            text = sprint.name,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Sprint Dates",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "${sprint.startDate.format(DateFormatter)} - ${sprint.endDate.format(DateFormatter)}",
                                style = MaterialTheme.typography.labelMedium,
                                color = MaterialTheme.colorScheme.outline
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Text(
                            text = "Goal",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = sprint.goal,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        progressState?.let { progress ->
                            AppCard(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Sprint Progress",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = "${progress.progressPercentage}%",
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                LinearProgressIndicator(
                                    progress = { progress.progressPercentage / 100f },
                                    modifier = Modifier.fillMaxWidth(),
                                    color = MaterialTheme.colorScheme.primary,
                                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Tasks: ${progress.completedTasks} completed / ${progress.totalTasks} total (${progress.inProgressTasks} in progress, ${progress.pendingTasks} pending)",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            Spacer(modifier = Modifier.height(16.dp))
                        }

                        TabRow(selectedTabIndex = selectedTab) {
                            Tab(
                                selected = selectedTab == 0,
                                onClick = { selectedTab = 0 },
                                text = { Text("Tasks (${state.assignedTasks.size})") }
                            )
                            Tab(
                                selected = selectedTab == 1,
                                onClick = { selectedTab = 1 },
                                text = { Text("User Stories (${state.assignedStories.size})") }
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (selectedTab == 0) {
                            if (state.assignedTasks.isEmpty()) {
                                Text(
                                    text = "No tasks assigned to this sprint yet.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            } else {
                                state.assignedTasks.forEach { task ->
                                    AppCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable { onTaskClick(task.id) }
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = task.title,
                                                style = MaterialTheme.typography.titleMedium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                text = task.status.name,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                        }
                                    }
                                }
                            }
                        } else {
                            if (state.assignedStories.isEmpty()) {
                                Text(
                                    text = "No user stories assigned to this sprint yet.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.padding(vertical = 16.dp)
                                )
                            } else {
                                state.assignedStories.forEach { story ->
                                    AppCard(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(vertical = 4.dp)
                                            .clickable { onStoryClick(story.id) }
                                    ) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(
                                                text = story.title,
                                                style = MaterialTheme.typography.titleMedium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis,
                                                modifier = Modifier.weight(1f)
                                            )
                                            Text(
                                                text = story.priority.name,
                                                style = MaterialTheme.typography.labelSmall,
                                                color = MaterialTheme.colorScheme.secondary
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AppButton(
                        text = "Edit Sprint",
                        onClick = { onEditClick(sprint.id) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AppButton(
                        text = "Delete Sprint",
                        onClick = {
                            viewModel.deleteSprint(sprint, onSuccess = onBackClick)
                        }
                    )
                }
            }
        }
    }
}
