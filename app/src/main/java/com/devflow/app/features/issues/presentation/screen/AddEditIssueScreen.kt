package com.devflow.app.features.issues.presentation.screen

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
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.issues.presentation.viewmodel.IssueViewModel

@Composable
fun AddEditIssueScreen(
    issueId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: IssueViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var priority by remember { mutableStateOf(IssuePriority.MEDIUM) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var showPriorityMenu by remember { mutableStateOf(false) }

    LaunchedEffect(issueId) {
        if (issueId != null) {
            viewModel.loadIssue(issueId)
        }
    }

    LaunchedEffect(state.selectedIssue) {
        state.selectedIssue?.let { issue ->
            if (issueId == issue.id) {
                title = issue.title
                description = issue.description ?: ""
                priority = issue.priority
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (issueId == null) "Log Defect" else "Edit Defect",
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
                label = "Issue Title (e.g. Login page crashes on click)"
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
                label = "Description / Defect Steps (Optional)",
                singleLine = false,
                minLines = 6
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
                    IssuePriority.entries.forEach { p ->
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
                            titleError = "Title cannot be empty"
                            hasError = true
                        }

                        if (!hasError) {
                            if (issueId == null) {
                                viewModel.addIssue(
                                    title = title,
                                    description = description,
                                    priority = priority,
                                    onSuccess = onNavigateBack
                                )
                            } else {
                                state.selectedIssue?.let { originalIssue ->
                                    val updatedIssue = originalIssue.copy(
                                        title = title.trim(),
                                        description = description.trim(),
                                        priority = priority
                                    )
                                    viewModel.editIssue(
                                        issue = updatedIssue,
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
