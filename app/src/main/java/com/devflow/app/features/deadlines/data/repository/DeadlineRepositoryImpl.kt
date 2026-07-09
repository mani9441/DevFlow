package com.devflow.app.features.deadlines.data.repository

import com.devflow.app.features.deadlines.data.local.dao.DeadlineDao
import com.devflow.app.features.deadlines.data.mapper.toDomain
import com.devflow.app.features.deadlines.data.mapper.toEntity
import com.devflow.app.features.deadlines.domain.model.Deadline
import com.devflow.app.features.deadlines.domain.repository.DeadlineRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class DeadlineRepositoryImpl @Inject constructor(
    private val dao: DeadlineDao
) : DeadlineRepository {

    override suspend fun createDeadline(deadline: Deadline): Long {
        require(deadline.title.isNotBlank()) { "Title cannot be empty" }

        val newDeadline = deadline.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return dao.insert(newDeadline.toEntity())
    }

    override suspend fun updateDeadline(deadline: Deadline) {
        require(deadline.title.isNotBlank()) { "Title cannot be empty" }

        val updatedDeadline = deadline.copy(
            updatedAt = LocalDateTime.now()
        )
        dao.update(updatedDeadline.toEntity())
    }

    override suspend fun deleteDeadline(deadline: Deadline) {
        dao.delete(deadline.toEntity())
    }

    override suspend fun getDeadline(id: Long): Deadline? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAllDeadlines(): Flow<List<Deadline>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
