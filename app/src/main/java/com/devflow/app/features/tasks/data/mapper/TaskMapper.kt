package com.devflow.app.features.tasks.data.mapper

import com.devflow.app.features.tasks.data.local.entity.TaskEntity
import com.devflow.app.features.tasks.domain.model.Task
import com.devflow.app.features.tasks.domain.model.TaskPriority
import com.devflow.app.features.tasks.domain.model.TaskStatus

fun TaskEntity.toDomain(): Task {
    return Task(
        id = id,
        projectId = projectId,
        title = title,
        description = description,
        priority = TaskPriority.valueOf(priority),
        status = TaskStatus.valueOf(status),
        dueDate = dueDate,
        sprintId = sprintId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Task.toEntity(): TaskEntity {
    return TaskEntity(
        id = id,
        projectId = projectId,
        title = title,
        description = description,
        priority = priority.name,
        status = status.name,
        dueDate = dueDate,
        sprintId = sprintId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
