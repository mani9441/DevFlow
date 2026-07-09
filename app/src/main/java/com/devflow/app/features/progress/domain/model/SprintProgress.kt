package com.devflow.app.features.progress.domain.model

/**
 * Calculated progress indicators of a Sprint (non-persistent).
 */
data class SprintProgress(
    val totalTasks: Int,
    val completedTasks: Int,
    val pendingTasks: Int,
    val inProgressTasks: Int,
    val progressPercentage: Int
)
