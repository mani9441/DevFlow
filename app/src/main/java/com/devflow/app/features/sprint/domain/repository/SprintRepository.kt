package com.devflow.app.features.sprint.domain.repository

import com.devflow.app.features.sprint.domain.model.Sprint
import kotlinx.coroutines.flow.Flow

interface SprintRepository {

    suspend fun createSprint(sprint: Sprint): Long

    suspend fun updateSprint(sprint: Sprint)

    suspend fun deleteSprint(sprint: Sprint)

    suspend fun getSprint(id: Long): Sprint?

    fun getAllSprints(projectId: Long): Flow<List<Sprint>>

    fun getAllSprints(): Flow<List<Sprint>> = kotlinx.coroutines.flow.flowOf(emptyList())
}
