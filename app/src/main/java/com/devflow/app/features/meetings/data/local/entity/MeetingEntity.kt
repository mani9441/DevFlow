package com.devflow.app.features.meetings.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.devflow.app.features.project.data.local.entity.ProjectEntity
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    tableName = "meetings",
    foreignKeys = [
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["projectId"])]
)
data class MeetingEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val projectId: Long,
    val title: String,
    val meetingDate: LocalDate,
    val meetingTime: String,
    val yesterdayWork: String,
    val todayPlan: String,
    val blockers: String,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
