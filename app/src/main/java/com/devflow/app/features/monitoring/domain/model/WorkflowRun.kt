package com.devflow.app.features.monitoring.domain.model

/**
 * Domain model representing a GitHub Actions Workflow Run.
 */
data class WorkflowRun(
    val id: Long,
    val workflowName: String,
    val status: String,
    val conclusion: String,
    val branch: String,
    val runNumber: Int,
    val event: String,
    val createdAt: String,
    val updatedAt: String,
    val url: String
)
