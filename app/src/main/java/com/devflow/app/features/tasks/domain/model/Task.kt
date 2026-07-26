package com.devflow.app.features.tasks.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

enum class TaskStatus {
    PENDING,
    IN_PROGRESS,
    COMPLETED
}

enum class TaskPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

/**
 * Domain model representing a Development Task in Agile domain.
 */
data class Task(
    val id: Long = 0L,
    val projectId: Long,
    val title: String,
    val description: String? = null,
    val priority: TaskPriority = TaskPriority.MEDIUM,
    val status: TaskStatus = TaskStatus.PENDING,
    val dueDate: LocalDate? = null,
    val sprintId: Long? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
