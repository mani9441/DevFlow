package com.devflow.app.features.tasks.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.app.core.designsystem.components.AppButton
import com.devflow.app.core.designsystem.components.AppCard
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.core.designsystem.components.EmptyState
import com.devflow.app.core.designsystem.components.LoadingView
import com.devflow.app.features.tasks.domain.model.TaskPriority
import com.devflow.app.features.tasks.domain.model.TaskStatus
import com.devflow.app.features.tasks.presentation.viewmodel.TaskViewModel
import java.time.format.DateTimeFormatter

private val DateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

@Composable
fun TaskDetailsScreen(
    taskId: Long,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    LaunchedEffect(taskId) {
        viewModel.loadTask(taskId)
    }

    val state = viewModel.uiState.collectAsState().value
    val task = state.selectedTask

    var showStatusMenu by remember { mutableStateOf(false) }
    var showSprintMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Task Details",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        when {
            state.loadingState -> {
                LoadingView()
            }
            task == null -> {
                EmptyState(message = "Task not found.")
            }
            else -> {
                val assignedSprint = state.availableSprints.find { it.id == task.sprintId }

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
                            text = task.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Status Selector Card
                        AppCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Status",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Text(
                                        text = task.status.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Box {
                                    AppButton(
                                        text = "Change Status",
                                        onClick = { showStatusMenu = true },
                                        modifier = Modifier.width(160.dp)
                                    )
                                    DropdownMenu(
                                        expanded = showStatusMenu,
                                        onDismissRequest = { showStatusMenu = false }
                                    ) {
                                        TaskStatus.entries.forEach { status ->
                                            DropdownMenuItem(
                                                text = { Text(status.name) },
                                                onClick = {
                                                    viewModel.updateStatus(task, status)
                                                    showStatusMenu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Sprint Assignment Card
                        AppCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Assigned Sprint",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Text(
                                        text = assignedSprint?.name ?: "Unassigned (Backlog)",
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                }
                                Box {
                                    AppButton(
                                        text = "Assign Sprint",
                                        onClick = { showSprintMenu = true },
                                        modifier = Modifier.width(160.dp)
                                    )
                                    DropdownMenu(
                                        expanded = showSprintMenu,
                                        onDismissRequest = { showSprintMenu = false }
                                    ) {
                                        DropdownMenuItem(
                                            text = { Text("Unassigned (Backlog)") },
                                            onClick = {
                                                viewModel.assignSprint(task, null)
                                                showSprintMenu = false
                                            }
                                        )
                                        state.availableSprints.forEach { sprint ->
                                            DropdownMenuItem(
                                                text = { Text(sprint.name) },
                                                onClick = {
                                                    viewModel.assignSprint(task, sprint.id)
                                                    showSprintMenu = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        // Details list
                        Text(
                            text = "Priority",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = task.priority.name,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Due Date",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = task.dueDate?.format(DateFormatter) ?: "No due date set",
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (task.description.isNullOrBlank()) "No description provided." else task.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AppButton(
                        text = "Edit Task Details",
                        onClick = { onEditClick(task.id) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AppButton(
                        text = "Delete Task",
                        onClick = {
                            viewModel.deleteTask(task, onSuccess = onBackClick)
                        }
                    )
                }
            }
        }
    }
}
