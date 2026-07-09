package com.devflow.app.features.sprint.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Domain model representing an Agile Sprint.
 */
data class Sprint(
    val id: Long = 0L,
    val name: String,
    val goal: String,
    val startDate: LocalDate,
    val endDate: LocalDate,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
