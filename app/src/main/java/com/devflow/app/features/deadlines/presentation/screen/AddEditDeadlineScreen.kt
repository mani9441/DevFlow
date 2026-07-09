package com.devflow.app.features.deadlines.presentation.screen

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
import com.devflow.app.features.deadlines.domain.model.Deadline
import com.devflow.app.features.deadlines.presentation.viewmodel.DeadlineViewModel
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Composable
fun AddEditDeadlineScreen(
    deadlineId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: DeadlineViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var dueDateStr by remember { mutableStateOf(LocalDate.now().toString()) }

    var titleError by remember { mutableStateOf<String?>(null) }
    var dateError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(deadlineId) {
        if (deadlineId != null) {
            viewModel.loadDeadline(deadlineId)
        }
    }

    LaunchedEffect(state.selectedDeadline) {
        state.selectedDeadline?.let { deadline ->
            if (deadlineId == deadline.id) {
                title = deadline.title
                description = deadline.description ?: ""
                dueDateStr = deadline.dueDate.toString()
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (deadlineId == null) "Add Deadline" else "Edit Deadline",
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
                label = "Title"
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

            AppTextField(
                value = dueDateStr,
                onValueChange = {
                    dueDateStr = it
                    if (dateError != null && it.isNotBlank()) {
                        dateError = null
                    }
                },
                label = "Due Date (YYYY-MM-DD)"
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
                            titleError = "Title cannot be empty"
                            hasError = true
                        }
                        var parsedDate: LocalDate? = null
                        try {
                            parsedDate = LocalDate.parse(dueDateStr.trim())
                        } catch (e: DateTimeParseException) {
                            dateError = "Invalid date format (must be YYYY-MM-DD)"
                            hasError = true
                        }

                        if (!hasError && parsedDate != null) {
                            if (deadlineId == null) {
                                viewModel.addDeadline(
                                    title = title,
                                    description = description,
                                    dueDate = parsedDate,
                                    onSuccess = onNavigateBack
                                )
                            } else {
                                state.selectedDeadline?.let { originalDeadline ->
                                    val updatedDeadline = originalDeadline.copy(
                                        title = title.trim(),
                                        description = description.trim(),
                                        dueDate = parsedDate
                                    )
                                    viewModel.editDeadline(
                                        deadline = updatedDeadline,
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
