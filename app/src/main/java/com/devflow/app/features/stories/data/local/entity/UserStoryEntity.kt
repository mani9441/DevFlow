package com.devflow.app.features.stories.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.devflow.app.features.sprint.data.local.entity.SprintEntity
import java.time.LocalDateTime

@Entity(
    tableName = "user_stories",
    foreignKeys = [
        ForeignKey(
            entity = SprintEntity::class,
            parentColumns = ["id"],
            childColumns = ["sprintId"],
            onDelete = ForeignKey.SET_NULL
        )
    ],
    indices = [Index(value = ["sprintId"])]
)
data class UserStoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String?,
    val acceptanceCriteria: String?,
    val priority: String, // mapped from enum name
    val sprintId: Long?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
