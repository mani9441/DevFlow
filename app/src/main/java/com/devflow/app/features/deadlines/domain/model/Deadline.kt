package com.devflow.app.features.deadlines.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Domain model representing an upcoming project Milestone / Deadline.
 */
data class Deadline(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val dueDate: LocalDate,
    val isCompleted: Boolean = false,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
