package com.devflow.app.features.stories.data.repository

import com.devflow.app.features.stories.data.local.dao.UserStoryDao
import com.devflow.app.features.stories.data.mapper.toDomain
import com.devflow.app.features.stories.data.mapper.toEntity
import com.devflow.app.features.stories.domain.model.UserStory
import com.devflow.app.features.stories.domain.repository.UserStoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class UserStoryRepositoryImpl @Inject constructor(
    private val dao: UserStoryDao
) : UserStoryRepository {

    override suspend fun createStory(story: UserStory): Long {
        require(story.title.isNotBlank()) { "Story title cannot be empty" }

        val newStory = story.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return dao.insert(newStory.toEntity())
    }

    override suspend fun updateStory(story: UserStory) {
        require(story.title.isNotBlank()) { "Story title cannot be empty" }

        val updatedStory = story.copy(
            updatedAt = LocalDateTime.now()
        )
        dao.update(updatedStory.toEntity())
    }

    override suspend fun deleteStory(story: UserStory) {
        dao.delete(story.toEntity())
    }

    override suspend fun getStory(id: Long): UserStory? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAllStories(projectId: Long): Flow<List<UserStory>> {
        return dao.getAll(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getStoriesBySprint(sprintId: Long): Flow<List<UserStory>> {
        return dao.getBySprint(sprintId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUnassignedStories(projectId: Long): Flow<List<UserStory>> {
        return dao.getUnassigned(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
