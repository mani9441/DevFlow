package com.devflow.app.features.issues.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(tableName = "issues")
data class IssueEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val title: String,
    val description: String?,
    val priority: String, // mapped from enum name
    val status: String,   // mapped from enum name
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
)
