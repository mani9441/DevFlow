package com.devflow.app.features.monitoring.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "repository_configs")
data class RepositoryConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val owner: String,
    val repository: String,
    val personalAccessToken: String?,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
