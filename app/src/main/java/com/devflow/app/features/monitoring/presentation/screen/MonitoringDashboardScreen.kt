package com.devflow.app.features.monitoring.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.app.core.designsystem.components.AppButton
import com.devflow.app.core.designsystem.components.AppCard
import com.devflow.app.core.designsystem.components.AppTextField
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.core.designsystem.components.EmptyState
import com.devflow.app.core.designsystem.components.LoadingView
import com.devflow.app.features.monitoring.presentation.viewmodel.DevelopmentMonitoringViewModel

@Composable
fun MonitoringDashboardScreen(
    onBuildClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: DevelopmentMonitoringViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value
    var isEditing by remember { mutableStateOf(false) }

    var owner by remember { mutableStateOf("") }
    var repository by remember { mutableStateOf("") }
    var token by remember { mutableStateOf("") }

    var ownerError by remember { mutableStateOf<String?>(null) }
    var repoError by remember { mutableStateOf<String?>(null) }

    // Sync input fields when configuration loads
    androidx.compose.runtime.LaunchedEffect(state.config) {
        state.config?.let { config ->
            owner = config.owner
            repository = config.repository
            token = config.personalAccessToken ?: ""
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Development Monitoring",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        val showForm = state.config == null || isEditing

        if (showForm) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Text(
                    text = "GitHub Repository Integration",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Text(
                    text = "Connect to a public or private GitHub repository to monitor CI workflows and build health status.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(bottom = 24.dp)
                )

                AppTextField(
                    value = owner,
                    onValueChange = {
                        owner = it
                        if (ownerError != null && it.isNotBlank()) ownerError = null
                    },
                    label = "Repository Owner (e.g. tensorflow)"
                )
                ownerError?.let { err ->
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    value = repository,
                    onValueChange = {
                        repository = it
                        if (repoError != null && it.isNotBlank()) repoError = null
                    },
                    label = "Repository Name (e.g. tensorflow)"
                )
                repoError?.let { err ->
                    Text(
                        text = err,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    value = token,
                    onValueChange = { token = it },
                    label = "Personal Access Token (PAT) (Optional for Public repos)"
                )

                Spacer(modifier = Modifier.height(24.dp))

                if (state.errorMessage != null) {
                    Text(
                        text = state.errorMessage,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    if (state.config != null) {
                        AppButton(
                            text = "Cancel",
                            onClick = {
                                isEditing = false
                                viewModel.clearError()
                            },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                    }
                    AppButton(
                        text = if (state.loadingBuilds) "Connecting..." else "Connect Repository",
                        onClick = {
                            var hasError = false
                            if (owner.isBlank()) {
                                ownerError = "Owner is required"
                                hasError = true
                            }
                            if (repository.isBlank()) {
                                repoError = "Repository name is required"
                                hasError = true
                            }

                            if (!hasError) {
                                viewModel.connectRepository(
                                    owner = owner.trim(),
                                    repoName = repository.trim(),
                                    token = token.trim(),
                                    onSuccess = {
                                        isEditing = false
                                    }
                                )
                            }
                        },
                        enabled = !state.loadingBuilds,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        } else {
            val config = state.config!!
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
            ) {
                AppCard(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Connected Repository",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.outline
                            )
                            Text(
                                text = "${config.owner}/${config.repository}",
                                style = MaterialTheme.typography.titleLarge,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                        IconButton(
                            onClick = { viewModel.refreshBuilds() }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Refresh",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AppButton(
                            text = "Manage Settings",
                            onClick = { isEditing = true },
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        AppButton(
                            text = "Disconnect",
                            onClick = { viewModel.removeRepository(config) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }

                state.errorMessage?.let { error ->
                    Text(
                        text = error,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }

                when {
                    state.loadingBuilds && state.workflowRuns.isEmpty() -> {
                        LoadingView()
                    }
                    state.workflowRuns.isEmpty() -> {
                        EmptyState(message = "No workflow runs found. Push code or configure repository workflows.")
                    }
                    else -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            items(state.workflowRuns) { build ->
                                AppCard(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            viewModel.selectRun(build)
                                            onBuildClick()
                                        }
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = build.workflowName,
                                                style = MaterialTheme.typography.titleMedium,
                                                maxLines = 1,
                                                overflow = TextOverflow.Ellipsis
                                            )
                                            Spacer(modifier = Modifier.height(4.dp))
                                            Text(
                                                text = "Branch: ${build.branch} • Run #${build.runNumber}",
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.outline
                                             )
                                        }

                                        Spacer(modifier = Modifier.width(16.dp))

                                        val conclusionLabel: String
                                        val badgeBgColor: Color
                                        val badgeTextColor: Color
                                        val iconVector: ImageVector
                                        when (build.conclusion) {
                                            "success" -> {
                                                conclusionLabel = "Passed"
                                                badgeBgColor = Color(0xFFDCFCE7)
                                                badgeTextColor = Color(0xFF15803D)
                                                iconVector = Icons.Default.CheckCircle
                                            }
                                            "failure", "cancelled" -> {
                                                conclusionLabel = "Failed"
                                                badgeBgColor = Color(0xFFFEE2E2)
                                                badgeTextColor = Color(0xFFB91C1C)
                                                iconVector = Icons.Default.Close
                                            }
                                            else -> {
                                                if (build.status == "in_progress") {
                                                    conclusionLabel = "Building"
                                                    badgeBgColor = Color(0xFFDBEAFE)
                                                    badgeTextColor = Color(0xFF1D4ED8)
                                                    iconVector = Icons.Default.Info
                                                } else {
                                                    conclusionLabel = "Pending"
                                                    badgeBgColor = Color(0xFFF1F5F9)
                                                    badgeTextColor = Color(0xFF475569)
                                                    iconVector = Icons.Default.Info
                                                }
                                            }
                                        }

                                        androidx.compose.material3.Card(
                                            colors = androidx.compose.material3.CardDefaults.cardColors(containerColor = badgeBgColor),
                                            shape = androidx.compose.foundation.shape.RoundedCornerShape(8.dp),
                                            elevation = androidx.compose.material3.CardDefaults.cardElevation(0.dp)
                                        ) {
                                            Row(
                                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                                            ) {
                                                if (build.status == "in_progress") {
                                                    CircularProgressIndicator(
                                                        modifier = Modifier.size(12.dp),
                                                        strokeWidth = 1.5.dp,
                                                        color = badgeTextColor
                                                    )
                                                } else {
                                                    Icon(
                                                        imageVector = iconVector,
                                                        contentDescription = conclusionLabel,
                                                        tint = badgeTextColor,
                                                        modifier = Modifier.size(14.dp)
                                                    )
                                                }
                                                Text(
                                                    text = conclusionLabel,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.ExtraBold,
                                                    color = badgeTextColor
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
