package com.devflow.app.features.issues.domain.model

import java.time.LocalDateTime

enum class IssueStatus {
    OPEN,
    IN_PROGRESS,
    RESOLVED,
    CLOSED
}

enum class IssuePriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

/**
 * Domain model representing a Software Defect / Issue.
 */
data class Issue(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val priority: IssuePriority = IssuePriority.MEDIUM,
    val status: IssueStatus = IssueStatus.OPEN,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
