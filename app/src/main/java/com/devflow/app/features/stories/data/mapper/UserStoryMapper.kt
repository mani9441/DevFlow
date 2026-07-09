package com.devflow.app.features.stories.data.mapper

import com.devflow.app.features.stories.data.local.entity.UserStoryEntity
import com.devflow.app.features.stories.domain.model.StoryPriority
import com.devflow.app.features.stories.domain.model.UserStory

fun UserStoryEntity.toDomain(): UserStory {
    return UserStory(
        id = id,
        title = title,
        description = description,
        acceptanceCriteria = acceptanceCriteria,
        priority = StoryPriority.valueOf(priority),
        sprintId = sprintId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun UserStory.toEntity(): UserStoryEntity {
    return UserStoryEntity(
        id = id,
        title = title,
        description = description,
        acceptanceCriteria = acceptanceCriteria,
        priority = priority.name,
        sprintId = sprintId,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
