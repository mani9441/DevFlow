package com.devflow.app.features.sprint.data.mapper

import com.devflow.app.features.sprint.data.local.entity.SprintEntity
import com.devflow.app.features.sprint.domain.model.Sprint

fun SprintEntity.toDomain(): Sprint {
    return Sprint(
        id = id,
        projectId = projectId,
        name = name,
        goal = goal,
        startDate = startDate,
        endDate = endDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Sprint.toEntity(): SprintEntity {
    return SprintEntity(
        id = id,
        projectId = projectId,
        name = name,
        goal = goal,
        startDate = startDate,
        endDate = endDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
