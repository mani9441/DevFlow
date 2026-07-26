package com.devflow.app.features.project.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.devflow.app.core.designsystem.components.AppButton
import com.devflow.app.core.designsystem.components.AppCard
import com.devflow.app.core.designsystem.components.AppTextField
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.features.project.domain.model.Project
import com.devflow.app.features.project.domain.model.ProjectStatus
import com.devflow.app.features.project.presentation.viewmodel.ProjectViewModel
import androidx.hilt.navigation.compose.hiltViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeParseException

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditProjectScreen(
    projectId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: ProjectViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var status by remember { mutableStateOf(ProjectStatus.PLANNING) }
    var startDateStr by remember { mutableStateOf(LocalDate.now().toString()) }
    var endDateStr by remember { mutableStateOf(LocalDate.now().plusMonths(3).toString()) }

    var showStartDatePicker by remember { mutableStateOf(false) }
    var showEndDatePicker by remember { mutableStateOf(false) }
    var showStatusDropdown by remember { mutableStateOf(false) }

    var nameError by remember { mutableStateOf<String?>(null) }
    var dateError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(projectId) {
        if (projectId != null) {
            viewModel.loadProject(projectId)
        }
    }

    LaunchedEffect(state.selectedProject) {
        state.selectedProject?.let { project ->
            if (projectId == project.id) {
                name = project.name
                description = project.description ?: ""
                status = project.status
                startDateStr = project.startDate?.toString() ?: ""
                endDateStr = project.endDate?.toString() ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (projectId == null) "Create Project" else "Edit Project Details",
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AppCard(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.fillMaxWidth()) {
                    AppTextField(
                        value = name,
                        onValueChange = {
                            name = it
                            if (nameError != null && it.isNotBlank()) {
                                nameError = null
                            }
                        },
                        label = "Project Workspace Name"
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
                        value = description,
                        onValueChange = { description = it },
                        label = "Project Scope / Description (Optional)",
                        singleLine = false,
                        minLines = 3
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Project Status Selector Dropdown
                    Box(modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = status.name.lowercase().replaceFirstChar { it.uppercase() },
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Workspace Lifecycle State") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showStatusDropdown = true },
                            trailingIcon = {
                                IconButton(onClick = { showStatusDropdown = true }) {
                                    Icon(
                                        imageVector = Icons.Default.KeyboardArrowDown,
                                        contentDescription = "Dropdown"
                                    )
                                }
                            }
                        )
                        DropdownMenu(
                            expanded = showStatusDropdown,
                            onDismissRequest = { showStatusDropdown = false }
                        ) {
                            ProjectStatus.values().forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option.name.lowercase().replaceFirstChar { it.uppercase() }) },
                                    onClick = {
                                        status = option
                                        showStatusDropdown = false
                                    }
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AppTextField(
                        value = startDateStr,
                        onValueChange = {
                            startDateStr = it
                            if (dateError != null) dateError = null
                        },
                        label = "Start Date (YYYY-MM-DD)",
                        trailingIcon = {
                            IconButton(onClick = { showStartDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select Start Date"
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    AppTextField(
                        value = endDateStr,
                        onValueChange = {
                            endDateStr = it
                            if (dateError != null) dateError = null
                        },
                        label = "Target Completion Date (YYYY-MM-DD)",
                        trailingIcon = {
                            IconButton(onClick = { showEndDatePicker = true }) {
                                Icon(
                                    imageVector = Icons.Default.DateRange,
                                    contentDescription = "Select End Date"
                                )
                            }
                        }
                    )

                    dateError?.let { error ->
                        Text(
                            text = error,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Save Workspace Settings",
                onClick = {
                    if (name.isBlank()) {
                        nameError = "Workspace name cannot be blank"
                        return@AppButton
                    }

                    val startLocalDate = if (startDateStr.isNotBlank()) {
                        try {
                            LocalDate.parse(startDateStr.trim())
                        } catch (e: DateTimeParseException) {
                            dateError = "Invalid start date format (use YYYY-MM-DD)"
                            return@AppButton
                        }
                    } else null

                    val endLocalDate = if (endDateStr.isNotBlank()) {
                        try {
                            LocalDate.parse(endDateStr.trim())
                        } catch (e: DateTimeParseException) {
                            dateError = "Invalid end date format (use YYYY-MM-DD)"
                            return@AppButton
                        }
                    } else null

                    if (startLocalDate != null && endLocalDate != null && endLocalDate.isBefore(startLocalDate)) {
                        dateError = "End date must be on or after start date"
                        return@AppButton
                    }

                    if (projectId == null) {
                        viewModel.createProject(
                            name = name,
                            description = description,
                            startDate = startLocalDate,
                            endDate = endLocalDate,
                            onSuccess = onNavigateBack
                        )
                    } else {
                        val existingProject = state.selectedProject
                        if (existingProject != null) {
                            viewModel.editProject(
                                project = existingProject.copy(
                                    name = name.trim(),
                                    description = description.trim(),
                                    status = status,
                                    startDate = startLocalDate,
                                    endDate = endLocalDate,
                                    updatedAt = LocalDateTime.now()
                                ),
                                onSuccess = onNavigateBack
                            )
                        }
                    }
                }
            )

            // Date Pickers Dialogs
            if (showStartDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = try {
                        val localDate = LocalDate.parse(startDateStr.trim())
                        val zoneId = java.time.ZoneId.systemDefault()
                        localDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
                    } catch (e: Exception) {
                        System.currentTimeMillis()
                    }
                )

                DatePickerDialog(
                    onDismissRequest = { showStartDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val instant = java.time.Instant.ofEpochMilli(millis)
                                val localDate = java.time.LocalDateTime.ofInstant(instant, java.time.ZoneId.of("UTC")).toLocalDate()
                                startDateStr = localDate.toString()
                            }
                            showStartDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showStartDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            if (showEndDatePicker) {
                val datePickerState = rememberDatePickerState(
                    initialSelectedDateMillis = try {
                        val localDate = LocalDate.parse(endDateStr.trim())
                        val zoneId = java.time.ZoneId.systemDefault()
                        localDate.atStartOfDay(zoneId).toInstant().toEpochMilli()
                    } catch (e: Exception) {
                        System.currentTimeMillis()
                    }
                )

                DatePickerDialog(
                    onDismissRequest = { showEndDatePicker = false },
                    confirmButton = {
                        TextButton(onClick = {
                            datePickerState.selectedDateMillis?.let { millis ->
                                val instant = java.time.Instant.ofEpochMilli(millis)
                                val localDate = java.time.LocalDateTime.ofInstant(instant, java.time.ZoneId.of("UTC")).toLocalDate()
                                endDateStr = localDate.toString()
                            }
                            showEndDatePicker = false
                        }) {
                            Text("OK")
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showEndDatePicker = false }) {
                            Text("Cancel")
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }
        }
    }
}
