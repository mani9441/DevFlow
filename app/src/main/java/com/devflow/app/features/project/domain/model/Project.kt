package com.devflow.app.features.project.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

data class Project(
    val id: Long = 0L,
    val name: String,
    val description: String?,
    val status: ProjectStatus,
    val startDate: LocalDate?,
    val endDate: LocalDate?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
