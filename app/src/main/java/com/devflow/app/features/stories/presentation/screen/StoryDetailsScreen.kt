package com.devflow.app.features.stories.presentation.screen

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
import com.devflow.app.features.stories.presentation.viewmodel.StoryViewModel

@Composable
fun StoryDetailsScreen(
    storyId: Long,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: StoryViewModel = hiltViewModel()
) {
    LaunchedEffect(storyId) {
        viewModel.loadStory(storyId)
    }

    val state = viewModel.uiState.collectAsState().value
    val story = state.selectedStory

    var showSprintMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Story Details",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        when {
            state.loadingState -> {
                LoadingView()
            }
            story == null -> {
                EmptyState(message = "User story not found.")
            }
            else -> {
                val assignedSprint = state.availableSprints.find { it.id == story.sprintId }

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
                            text = story.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

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
                                                viewModel.assignSprint(story, null)
                                                showSprintMenu = false
                                            }
                                        )
                                        state.availableSprints.forEach { sprint ->
                                            DropdownMenuItem(
                                                text = { Text(sprint.name) },
                                                onClick = {
                                                    viewModel.assignSprint(story, sprint.id)
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

                        Text(
                            text = "Priority",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = story.priority.name,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "User Story / Description",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (story.description.isNullOrBlank()) "No description provided." else story.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Acceptance Criteria",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (story.acceptanceCriteria.isNullOrBlank()) "No acceptance criteria provided." else story.acceptanceCriteria,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AppButton(
                        text = "Edit Story Details",
                        onClick = { onEditClick(story.id) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AppButton(
                        text = "Delete Story",
                        onClick = {
                            viewModel.deleteStory(story, onSuccess = onBackClick)
                        }
                    )
                }
            }
        }
    }
}
