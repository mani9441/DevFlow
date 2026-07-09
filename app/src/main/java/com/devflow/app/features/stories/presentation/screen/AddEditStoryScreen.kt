package com.devflow.app.features.stories.presentation.screen

import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import com.devflow.app.core.designsystem.components.AppTextField
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.features.stories.domain.model.UserStory
import com.devflow.app.features.stories.domain.model.StoryPriority
import com.devflow.app.features.stories.presentation.viewmodel.StoryViewModel

@Composable
fun AddEditStoryScreen(
    storyId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: StoryViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var acceptanceCriteria by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(StoryPriority.MEDIUM) }
    var sprintId by remember { mutableStateOf<Long?>(null) }

    var titleError by remember { mutableStateOf<String?>(null) }

    var showPriorityMenu by remember { mutableStateOf(false) }
    var showSprintMenu by remember { mutableStateOf(false) }

    LaunchedEffect(storyId) {
        if (storyId != null) {
            viewModel.loadStory(storyId)
        }
    }

    LaunchedEffect(state.selectedStory) {
        state.selectedStory?.let { story ->
            if (storyId == story.id) {
                title = story.title
                description = story.description ?: ""
                acceptanceCriteria = story.acceptanceCriteria ?: ""
                priority = story.priority
                sprintId = story.sprintId
            }
        }
    }

    val selectedSprintName = state.availableSprints.find { it.id == sprintId }?.name ?: "Unassigned (Backlog)"

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (storyId == null) "Create Story" else "Edit Story",
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AppTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (titleError != null && it.isNotBlank()) {
                        titleError = null
                    }
                },
                label = "Story Title (e.g. As a customer, I want...)"
            )
            titleError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = description,
                onValueChange = { description = it },
                label = "Description / User Story (Optional)",
                singleLine = false,
                minLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = acceptanceCriteria,
                onValueChange = { acceptanceCriteria = it },
                label = "Acceptance Criteria (Optional)",
                singleLine = false,
                minLines = 4
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Priority Selector dropdown
            Box {
                OutlinedTextField(
                    value = priority.name,
                    onValueChange = {},
                    label = { Text("Priority") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showPriorityMenu = true },
                    enabled = false,
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                DropdownMenu(
                    expanded = showPriorityMenu,
                    onDismissRequest = { showPriorityMenu = false }
                ) {
                    StoryPriority.entries.forEach { p ->
                        DropdownMenuItem(
                            text = { Text(p.name) },
                            onClick = {
                                priority = p
                                showPriorityMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Sprint Selector dropdown
            Box {
                OutlinedTextField(
                    value = selectedSprintName,
                    onValueChange = {},
                    label = { Text("Sprint Assignment") },
                    readOnly = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { showSprintMenu = true },
                    enabled = false,
                    colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                        disabledTextColor = MaterialTheme.colorScheme.onSurface,
                        disabledBorderColor = MaterialTheme.colorScheme.outline,
                        disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                )
                DropdownMenu(
                    expanded = showSprintMenu,
                    onDismissRequest = { showSprintMenu = false }
                ) {
                    DropdownMenuItem(
                        text = { Text("Unassigned (Backlog)") },
                        onClick = {
                            sprintId = null
                            showSprintMenu = false
                        }
                    )
                    state.availableSprints.forEach { sprint ->
                        DropdownMenuItem(
                            text = { Text(sprint.name) },
                            onClick = {
                                sprintId = sprint.id
                                showSprintMenu = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AppButton(
                    text = "Cancel",
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                AppButton(
                    text = "Save",
                    onClick = {
                        var hasError = false
                        if (title.isBlank()) {
                            titleError = "Story title cannot be empty"
                            hasError = true
                        }

                        if (!hasError) {
                            if (storyId == null) {
                                viewModel.addStory(
                                    title = title,
                                    description = description,
                                    acceptanceCriteria = acceptanceCriteria,
                                    priority = priority,
                                    sprintId = sprintId,
                                    onSuccess = onNavigateBack
                                )
                            } else {
                                state.selectedStory?.let { originalStory ->
                                    val updatedStory = originalStory.copy(
                                        title = title.trim(),
                                        description = description.trim(),
                                        acceptanceCriteria = acceptanceCriteria.trim(),
                                        priority = priority,
                                        sprintId = sprintId
                                    )
                                    viewModel.editStory(
                                        story = updatedStory,
                                        onSuccess = onNavigateBack
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
