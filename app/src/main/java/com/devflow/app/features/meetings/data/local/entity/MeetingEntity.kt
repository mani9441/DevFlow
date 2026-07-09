package com.devflow.app.features.meetings.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "meetings")
data class MeetingEntity(
    @PrimaryKey(autoGenerate = true)
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
