package com.devflow.app.features.monitoring.domain.model

import java.time.LocalDateTime

data class RepositoryConfig(
    val id: Long = 0L,
    val projectId: Long,
    val owner: String,
    val repository: String,
    val personalAccessToken: String? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
