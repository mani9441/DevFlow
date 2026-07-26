package com.devflow.app.features.project.data.mapper

import com.devflow.app.features.project.data.local.entity.ProjectEntity
import com.devflow.app.features.project.domain.model.Project
import com.devflow.app.features.project.domain.model.ProjectStatus

fun ProjectEntity.toDomain(): Project {
    return Project(
        id = id,
        name = name,
        description = description,
        status = try { ProjectStatus.valueOf(status) } catch (e: Exception) { ProjectStatus.PLANNING },
        startDate = startDate,
        endDate = endDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Project.toEntity(): ProjectEntity {
    return ProjectEntity(
        id = id,
        name = name,
        description = description,
        status = status.name,
        startDate = startDate,
        endDate = endDate,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
