package com.devflow.app.features.monitoring.presentation.state

import com.devflow.app.features.monitoring.domain.model.WorkflowRun
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig

data class MonitoringUiState(
    val config: RepositoryConfig? = null,
    val workflowRuns: List<WorkflowRun> = emptyList(),
    val selectedRun: WorkflowRun? = null,
    val loadingConfig: Boolean = false,
    val loadingBuilds: Boolean = false,
    val errorMessage: String? = null
)
