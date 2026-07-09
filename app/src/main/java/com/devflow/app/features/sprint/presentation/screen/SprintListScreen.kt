package com.devflow.app.features.sprint.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.app.core.designsystem.components.AppCard
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.core.designsystem.components.EmptyState
import com.devflow.app.core.designsystem.components.LoadingView
import com.devflow.app.features.sprint.presentation.viewmodel.SprintViewModel
import java.time.format.DateTimeFormatter

private val DateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

@Composable
fun SprintListScreen(
    onAddSprint: () -> Unit,
    onSprintClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SprintViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Agile Sprints",
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddSprint,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Sprint"
                )
            }
        }
    ) { padding ->
        when {
            state.loadingState -> {
                LoadingView()
            }
            state.sprintList.isEmpty() -> {
                EmptyState(message = "No sprints created yet. Tap + to start planning!")
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.sprintList, key = { it.id }) { sprint ->
                        AppCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    onSprintClick(sprint.id)
                                }
                        ) {
                            Text(
                                text = sprint.name,
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Goal: ${sprint.goal}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "${sprint.startDate.format(DateFormatter)} - ${sprint.endDate.format(DateFormatter)}",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.outline
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
