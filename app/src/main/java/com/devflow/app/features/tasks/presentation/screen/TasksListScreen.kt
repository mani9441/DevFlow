package com.devflow.app.features.tasks.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import com.devflow.app.core.designsystem.components.AppCard
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.core.designsystem.components.EmptyState
import com.devflow.app.core.designsystem.components.LoadingView
import com.devflow.app.features.tasks.presentation.viewmodel.TaskViewModel

@Composable
fun TasksListScreen(
    onAddTask: () -> Unit,
    onTaskClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    var selectedTab by remember { mutableIntStateOf(0) } // 0: All, 1: Backlog (Unassigned)

    val sprintMap = state.availableSprints.associate { it.id to it.name }
    val displayedTasks = if (selectedTab == 0) state.taskList else state.unassignedTasks

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Development Tasks",
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddTask,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Task"
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            TabRow(selectedTabIndex = selectedTab) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = { Text("All Tasks (${state.taskList.size})") }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = { Text("Product Backlog (${state.unassignedTasks.size})") }
                )
            }

            when {
                state.loadingState -> {
                    LoadingView()
                }
                displayedTasks.isEmpty() -> {
                    EmptyState(message = if (selectedTab == 0) "No tasks logged yet. Tap + to add one!" else "No unassigned backlog tasks.")
                }
                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(displayedTasks, key = { it.id }) { task ->
                            AppCard(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable {
                                        onTaskClick(task.id)
                                    }
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = task.title,
                                        style = MaterialTheme.typography.titleLarge,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis,
                                        modifier = Modifier.weight(1f)
                                    )
                                    Text(
                                        text = task.priority.name,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = when (task.priority.name) {
                                            "CRITICAL" -> MaterialTheme.colorScheme.error
                                            "HIGH" -> MaterialTheme.colorScheme.tertiary
                                            else -> MaterialTheme.colorScheme.outline
                                        }
                                    )
                                }

                                task.description?.let { desc ->
                                    if (desc.isNotBlank()) {
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = desc,
                                            style = MaterialTheme.typography.bodyMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                                            maxLines = 2,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(12.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Sprint: ${sprintMap[task.sprintId] ?: "Unassigned (Backlog)"}",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )

                                    Text(
                                        text = task.status.name,
                                        style = MaterialTheme.typography.labelMedium,
                                        color = when (task.status.name) {
                                            "COMPLETED" -> MaterialTheme.colorScheme.primary
                                            "IN_PROGRESS" -> MaterialTheme.colorScheme.secondary
                                            else -> MaterialTheme.colorScheme.outline
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
