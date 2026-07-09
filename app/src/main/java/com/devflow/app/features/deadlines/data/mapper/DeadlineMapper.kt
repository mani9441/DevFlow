package com.devflow.app.features.deadlines.data.mapper

import com.devflow.app.features.deadlines.data.local.entity.DeadlineEntity
import com.devflow.app.features.deadlines.domain.model.Deadline

fun DeadlineEntity.toDomain(): Deadline {
    return Deadline(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        isCompleted = isCompleted,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Deadline.toEntity(): DeadlineEntity {
    return DeadlineEntity(
        id = id,
        title = title,
        description = description,
        dueDate = dueDate,
        isCompleted = isCompleted,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
