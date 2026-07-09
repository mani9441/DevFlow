package com.devflow.app.features.stories.domain.model

import java.time.LocalDateTime

enum class StoryPriority {
    LOW,
    MEDIUM,
    HIGH,
    CRITICAL
}

/**
 * Domain model representing a User Story in Agile domain.
 */
data class UserStory(
    val id: Long = 0L,
    val title: String,
    val description: String? = null,
    val acceptanceCriteria: String? = null,
    val priority: StoryPriority = StoryPriority.MEDIUM,
    val sprintId: Long? = null,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
