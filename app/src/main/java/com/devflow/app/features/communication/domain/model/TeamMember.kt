package com.devflow.app.features.communication.domain.model

/**
 * Domain model representing a project Team Member.
 */
data class TeamMember(
    val id: Long = 0L,
    val projectId: Long,
    val name: String,
    val role: String
)
