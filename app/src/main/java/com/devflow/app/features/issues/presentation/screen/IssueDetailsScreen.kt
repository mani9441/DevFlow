package com.devflow.app.features.issues.presentation.screen

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
import com.devflow.app.features.issues.domain.model.IssueStatus
import com.devflow.app.features.issues.presentation.viewmodel.IssueViewModel
import java.time.format.DateTimeFormatter

private val DateFormatter = DateTimeFormatter.ofPattern("MMM dd, yyyy, hh:mm a")

@Composable
fun IssueDetailsScreen(
    issueId: Long,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: IssueViewModel = hiltViewModel()
) {
    LaunchedEffect(issueId) {
        viewModel.loadIssue(issueId)
    }

    val state = viewModel.uiState.collectAsState().value
    val issue = state.selectedIssue

    var showStatusMenu by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Issue Details",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        when {
            state.loadingState -> {
                LoadingView()
            }
            issue == null -> {
                EmptyState(message = "Issue not found.")
            }
            else -> {
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
                            text = "Issue #${issue.id}: ${issue.title}",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        AppCard(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Column {
                                    Text(
                                        text = "Status",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.outline
                                    )
                                    Text(
                                        text = issue.status.name,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                                Box {
                                    AppButton(
                                        text = "Change Status",
                                        onClick = { showStatusMenu = true },
                                        modifier = Modifier.width(160.dp)
                                    )
                                    DropdownMenu(
                                        expanded = showStatusMenu,
                                        onDismissRequest = { showStatusMenu = false }
                                    ) {
                                        IssueStatus.entries.forEach { status ->
                                            DropdownMenuItem(
                                                text = { Text(status.name) },
                                                onClick = {
                                                    viewModel.changeStatus(issue, status)
                                                    showStatusMenu = false
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
                            text = issue.priority.name,
                            style = MaterialTheme.typography.bodyLarge
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Description",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            text = if (issue.description.isNullOrBlank()) "No description provided." else issue.description,
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(20.dp))
                        HorizontalDivider()
                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Logged: ${issue.createdAt.format(DateFormatter)}",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.outline
                        )
                        if (issue.updatedAt.isAfter(issue.createdAt)) {
                            Text(
                                text = "Updated: ${issue.updatedAt.format(DateFormatter)}",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    AppButton(
                        text = "Edit Issue Details",
                        onClick = { onEditClick(issue.id) }
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AppButton(
                        text = "Delete Issue",
                        onClick = {
                            viewModel.deleteIssue(issue, onSuccess = onBackClick)
                        }
                    )
                }
            }
        }
    }
}
