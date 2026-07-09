package com.devflow.app.features.notes.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing a Project Note.
 */
data class Note(
    val id: Long = 0L,
    val title: String,
    val content: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
