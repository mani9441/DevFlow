package com.devflow.app.features.issues.presentation.screen

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
import com.devflow.app.features.issues.presentation.viewmodel.IssueViewModel

@Composable
fun IssuesListScreen(
    onAddIssue: () -> Unit,
    onIssueClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    viewModel: IssueViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Issue Tracking",
                onBackClick = onBackClick
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddIssue,
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add Issue"
                )
            }
        }
    ) { padding ->
        when {
            state.loadingState && state.issueList.isEmpty() -> {
                LoadingView()
            }
            state.issueList.isEmpty() -> {
                EmptyState(message = "No issues logged yet. Tap + to log a defect!")
            }
            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(state.issueList, key = { it.id }) { issue ->
                        AppCard(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.selectIssue(issue)
                                    onIssueClick(issue.id)
                                }
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Issue #${issue.id}: ${issue.title}",
                                    style = MaterialTheme.typography.titleLarge,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis,
                                    modifier = Modifier.weight(1f)
                                )
                                Text(
                                    text = issue.priority.name,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = when (issue.priority.name) {
                                        "CRITICAL" -> MaterialTheme.colorScheme.error
                                        "HIGH" -> MaterialTheme.colorScheme.tertiary
                                        else -> MaterialTheme.colorScheme.outline
                                    }
                                )
                            }

                            issue.description?.let { desc ->
                                if (desc.isNotBlank()) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = desc,
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        maxLines = 2,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                Text(
                                    text = issue.status.name,
                                    style = MaterialTheme.typography.labelMedium,
                                    color = when (issue.status.name) {
                                        "RESOLVED", "CLOSED" -> MaterialTheme.colorScheme.primary
                                        "IN_PROGRESS" -> MaterialTheme.colorScheme.secondary
                                        else -> MaterialTheme.colorScheme.outline
                                    }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
