package com.devflow.app.features.sprint.data.repository

import com.devflow.app.features.sprint.data.local.dao.SprintDao
import com.devflow.app.features.sprint.data.mapper.toDomain
import com.devflow.app.features.sprint.data.mapper.toEntity
import com.devflow.app.features.sprint.domain.model.Sprint
import com.devflow.app.features.sprint.domain.repository.SprintRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class SprintRepositoryImpl @Inject constructor(
    private val dao: SprintDao
) : SprintRepository {

    override suspend fun createSprint(sprint: Sprint): Long {
        require(sprint.name.isNotBlank()) { "Sprint name cannot be empty" }
        require(sprint.goal.isNotBlank()) { "Sprint goal cannot be empty" }
        require(!sprint.endDate.isBefore(sprint.startDate)) { "End date must be on or after start date" }

        val newSprint = sprint.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return dao.insert(newSprint.toEntity())
    }

    override suspend fun updateSprint(sprint: Sprint) {
        require(sprint.name.isNotBlank()) { "Sprint name cannot be empty" }
        require(sprint.goal.isNotBlank()) { "Sprint goal cannot be empty" }
        require(!sprint.endDate.isBefore(sprint.startDate)) { "End date must be on or after start date" }

        val updatedSprint = sprint.copy(
            updatedAt = LocalDateTime.now()
        )
        dao.update(updatedSprint.toEntity())
    }

    override suspend fun deleteSprint(sprint: Sprint) {
        dao.delete(sprint.toEntity())
    }

    override suspend fun getSprint(id: Long): Sprint? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAllSprints(projectId: Long): Flow<List<Sprint>> {
        return dao.getAll(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllSprints(): Flow<List<Sprint>> {
        return dao.getAllSprints().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
