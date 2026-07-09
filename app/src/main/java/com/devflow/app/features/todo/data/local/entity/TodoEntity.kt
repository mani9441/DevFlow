package com.devflow.app.features.todo.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(tableName = "todos")
data class TodoEntity(

    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    val title: String,

    val description: String?,

    val dueDate: LocalDate?,

    val status: String,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)