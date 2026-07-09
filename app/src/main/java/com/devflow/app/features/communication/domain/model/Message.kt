package com.devflow.app.features.communication.domain.model

import java.time.LocalDateTime

/**
 * Domain model representing a chat Message.
 */
data class Message(
    val id: Long = 0L,
    val senderId: Long,
    val receiverId: Long,
    val message: String,
    val sentAt: LocalDateTime
)
