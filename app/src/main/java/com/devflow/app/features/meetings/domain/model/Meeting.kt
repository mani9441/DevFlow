package com.devflow.app.features.meetings.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Domain model representing a Stand-up Meeting.
 */
data class Meeting(
    val id: Long = 0L,
    val title: String,
    val meetingDate: LocalDate,
    val meetingTime: String,
    val yesterdayWork: String,
    val todayPlan: String,
    val blockers: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
