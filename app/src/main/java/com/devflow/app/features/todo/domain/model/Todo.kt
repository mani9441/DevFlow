package com.devflow.app.features.todo.domain.model

import java.time.LocalDate
import java.time.LocalDateTime

/**
 * Domain model used throughout the application.
 * Independent from the database implementation.
 */
data class Todo(

    val id: Long = 0L,

    val title: String,

    val description: String? = null,

    val dueDate: LocalDate? = null,

    val status: TodoStatus = TodoStatus.PENDING,

    val createdAt: LocalDateTime,

    val updatedAt: LocalDateTime
)