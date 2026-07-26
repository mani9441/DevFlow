package com.devflow.app.features.issues.data.mapper

import com.devflow.app.features.issues.data.local.entity.IssueEntity
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.issues.domain.model.IssueStatus

fun IssueEntity.toDomain(): Issue {
    return Issue(
        id = id,
        projectId = projectId,
        title = title,
        description = description,
        priority = IssuePriority.valueOf(priority),
        status = IssueStatus.valueOf(status),
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Issue.toEntity(): IssueEntity {
    return IssueEntity(
        id = id,
        projectId = projectId,
        title = title,
        description = description,
        priority = priority.name,
        status = status.name,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
