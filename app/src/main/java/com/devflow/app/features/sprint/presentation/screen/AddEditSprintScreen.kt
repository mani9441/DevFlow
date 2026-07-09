package com.devflow.app.features.sprint.presentation.screen

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
import com.devflow.app.core.designsystem.components.AppTextField
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.features.sprint.domain.model.Sprint
import com.devflow.app.features.sprint.presentation.viewmodel.SprintViewModel
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Composable
fun AddEditSprintScreen(
    sprintId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: SprintViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    var name by remember { mutableStateOf("") }
    var goal by remember { mutableStateOf("") }
    var startDateStr by remember { mutableStateOf(LocalDate.now().toString()) }
    var endDateStr by remember { mutableStateOf(LocalDate.now().plusDays(14).toString()) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var goalError by remember { mutableStateOf<String?>(null) }
    var dateError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(sprintId) {
        if (sprintId != null) {
            viewModel.loadSprint(sprintId)
        }
    }

    LaunchedEffect(state.selectedSprint) {
        state.selectedSprint?.let { sprint ->
            if (sprintId == sprint.id) {
                name = sprint.name
                goal = sprint.goal
                startDateStr = sprint.startDate.toString()
                endDateStr = sprint.endDate.toString()
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (sprintId == null) "Create Sprint" else "Edit Sprint",
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
                value = name,
                onValueChange = {
                    name = it
                    if (nameError != null && it.isNotBlank()) {
                        nameError = null
                    }
                },
                label = "Sprint Name (e.g. Sprint 15)"
            )
            nameError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = goal,
                onValueChange = {
                    goal = it
                    if (goalError != null && it.isNotBlank()) {
                        goalError = null
                    }
                },
                label = "Sprint Goal (e.g. Authentication Module)"
            )
            goalError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = startDateStr,
                onValueChange = {
                    startDateStr = it
                    if (dateError != null && it.isNotBlank()) {
                        dateError = null
                    }
                },
                label = "Start Date (YYYY-MM-DD)"
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = endDateStr,
                onValueChange = {
                    endDateStr = it
                    if (dateError != null && it.isNotBlank()) {
                        dateError = null
                    }
                },
                label = "End Date (YYYY-MM-DD)"
            )
            dateError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
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
                        if (name.isBlank()) {
                            nameError = "Sprint name cannot be empty"
                            hasError = true
                        }
                        if (goal.isBlank()) {
                            goalError = "Sprint goal cannot be empty"
                            hasError = true
                        }

                        var parsedStart: LocalDate? = null
                        var parsedEnd: LocalDate? = null
                        try {
                            parsedStart = LocalDate.parse(startDateStr.trim())
                            parsedEnd = LocalDate.parse(endDateStr.trim())
                            if (parsedEnd.isBefore(parsedStart)) {
                                dateError = "End date must be on or after start date"
                                hasError = true
                            }
                        } catch (e: DateTimeParseException) {
                            dateError = "Invalid date format (must be YYYY-MM-DD)"
                            hasError = true
                        }

                        if (!hasError && parsedStart != null && parsedEnd != null) {
                            if (sprintId == null) {
                                viewModel.addSprint(
                                    name = name,
                                    goal = goal,
                                    startDate = parsedStart,
                                    endDate = parsedEnd,
                                    onSuccess = onNavigateBack
                                )
                            } else {
                                state.selectedSprint?.let { originalSprint ->
                                    val updatedSprint = originalSprint.copy(
                                        name = name.trim(),
                                        goal = goal.trim(),
                                        startDate = parsedStart,
                                        endDate = parsedEnd
                                    )
                                    viewModel.editSprint(
                                        sprint = updatedSprint,
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
