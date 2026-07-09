package com.devflow.app.features.communication.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    tableName = "messages",
    foreignKeys = [
        ForeignKey(
            entity = TeamMemberEntity::class,
            parentColumns = ["id"],
            childColumns = ["senderId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = TeamMemberEntity::class,
            parentColumns = ["id"],
            childColumns = ["receiverId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["senderId"]),
        Index(value = ["receiverId"])
    ]
)
data class MessageEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val senderId: Long,
    val receiverId: Long,
    val message: String,
    val sentAt: LocalDateTime
)
