package com.devflow.app.features.deadlines.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.app.core.designsystem.components.AppButton
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.core.designsystem.components.EmptyState
import com.devflow.app.core.designsystem.components.LoadingView
import com.devflow.app.features.deadlines.presentation.viewmodel.DeadlineViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

private val DateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy")

@Composable
fun DeadlineDetailsScreen(
    deadlineId: Long,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: DeadlineViewModel = hiltViewModel()
) {
    LaunchedEffect(deadlineId) {
        viewModel.loadDeadline(deadlineId)
    }

    val state = viewModel.uiState.collectAsState().value
    val deadline = state.selectedDeadline
    val today = LocalDate.now()

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Deadline Details",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        when {
            state.loadingState -> {
                LoadingView()
            }
            deadline == null -> {
                EmptyState(message = "Deadline not found.")
            }
            else -> {
                val isOverdue = deadline.dueDate.isBefore(today) && !deadline.isCompleted

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
                            text = deadline.title,
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Metadata (Due Date & Status)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.DateRange,
                                contentDescription = "Due Date",
                                tint = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Due Date: ${deadline.dueDate.format(DateFormatter)}",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (isOverdue) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.outline
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(
                                imageVector = Icons.Default.Info,
                                contentDescription = "Status",
                                tint = MaterialTheme.colorScheme.outline,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Status: ${if (deadline.isCompleted) "Completed" else "Active"}",
                                style = MaterialTheme.typography.labelMedium,
                                color = if (deadline.isCompleted) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (deadline.description.isNullOrBlank()) "No description provided." else deadline.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AppButton(
                        text = if (deadline.isCompleted) "Mark Active" else "Mark Completed",
                        onClick = {
                            viewModel.toggleCompletion(deadline)
                        }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppButton(
                        text = "Edit",
                        onClick = { onEditClick(deadline.id) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppButton(
                        text = "Delete",
                        onClick = {
                            viewModel.deleteDeadline(deadline, onSuccess = onBackClick)
                        }
                    )
                }
            }
        }
    }
}
