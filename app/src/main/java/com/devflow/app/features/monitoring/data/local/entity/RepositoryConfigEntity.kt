package com.devflow.app.features.monitoring.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.devflow.app.features.project.data.local.entity.ProjectEntity
import java.time.LocalDateTime

@Entity(
    tableName = "repository_configs",
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
data class RepositoryConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val projectId: Long,
    val owner: String,
    val repository: String,
    val personalAccessToken: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
