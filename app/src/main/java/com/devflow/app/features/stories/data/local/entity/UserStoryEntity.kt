package com.devflow.app.features.stories.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.devflow.app.features.sprint.data.local.entity.SprintEntity
import com.devflow.app.features.project.data.local.entity.ProjectEntity
import java.time.LocalDateTime

@Entity(
    tableName = "user_stories",
    foreignKeys = [
        ForeignKey(
            entity = SprintEntity::class,
            parentColumns = ["id"],
            childColumns = ["sprintId"],
            onDelete = ForeignKey.SET_NULL
        ),
        ForeignKey(
            entity = ProjectEntity::class,
            parentColumns = ["id"],
            childColumns = ["projectId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["sprintId"]),
        Index(value = ["projectId"])
    ]
)
data class UserStoryEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val projectId: Long,
    val title: String,
    val description: String?,
    val acceptanceCriteria: String?,
    val priority: String,
    val sprintId: Long?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
