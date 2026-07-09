package com.devflow.app.features.stories.domain.repository

import com.devflow.app.features.stories.domain.model.UserStory
import kotlinx.coroutines.flow.Flow

interface UserStoryRepository {

    suspend fun createStory(story: UserStory): Long

    suspend fun updateStory(story: UserStory)

    suspend fun deleteStory(story: UserStory)

    suspend fun getStory(id: Long): UserStory?

    fun getAllStories(): Flow<List<UserStory>>

    fun getStoriesBySprint(sprintId: Long): Flow<List<UserStory>>

    fun getUnassignedStories(): Flow<List<UserStory>>
}
