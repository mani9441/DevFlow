package com.devflow.app.features.tasks.presentation.screen

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
import com.devflow.app.features.tasks.domain.model.Task
import com.devflow.app.features.tasks.domain.model.TaskPriority
import com.devflow.app.features.tasks.presentation.viewmodel.TaskViewModel
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Composable
fun AddEditTaskScreen(
    taskId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: TaskViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(TaskPriority.MEDIUM) }
    var dueDateStr by remember { mutableStateOf("") }
    var sprintId by remember { mutableStateOf<Long?>(null) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var dateError by remember { mutableStateOf<String?>(null) }

    var showPriorityMenu by remember { mutableStateOf(false) }
    var showSprintMenu by remember { mutableStateOf(false) }

    LaunchedEffect(taskId) {
        if (taskId != null) {
            viewModel.loadTask(taskId)
        }
    }

    LaunchedEffect(state.selectedTask) {
        state.selectedTask?.let { task ->
            if (taskId == task.id) {
                title = task.title
                description = task.description ?: ""
                priority = task.priority
                dueDateStr = task.dueDate?.toString() ?: ""
                sprintId = task.sprintId
            }
        }
    }

    val selectedSprintName = state.availableSprints.find { it.id == sprintId }?.name ?: "Unassigned (Backlog)"

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (taskId == null) "Create Task" else "Edit Task",
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
                label = "Task Title (e.g. Implement Login API)"
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
                label = "Description (Optional)",
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
                    enabled = false, // keeps keyboard from showing up
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
                    TaskPriority.entries.forEach { p ->
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

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = dueDateStr,
                onValueChange = {
                    dueDateStr = it
                    if (dateError != null && it.isNotBlank()) {
                        dateError = null
                    }
                },
                label = "Due Date (YYYY-MM-DD, Optional)"
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
                        if (title.isBlank()) {
                            titleError = "Task title cannot be empty"
                            hasError = true
                        }

                        var parsedDate: LocalDate? = null
                        if (dueDateStr.isNotBlank()) {
                            try {
                                parsedDate = LocalDate.parse(dueDateStr.trim())
                            } catch (e: DateTimeParseException) {
                                dateError = "Invalid date format (must be YYYY-MM-DD)"
                                hasError = true
                            }
                        }

                        if (!hasError) {
                            if (taskId == null) {
                                viewModel.addTask(
                                    title = title,
                                    description = description,
                                    priority = priority,
                                    dueDate = parsedDate,
                                    sprintId = sprintId,
                                    onSuccess = onNavigateBack
                                )
                            } else {
                                state.selectedTask?.let { originalTask ->
                                    val updatedTask = originalTask.copy(
                                        title = title.trim(),
                                        description = description.trim(),
                                        priority = priority,
                                        dueDate = parsedDate,
                                        sprintId = sprintId
                                    )
                                    viewModel.editTask(
                                        task = updatedTask,
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
