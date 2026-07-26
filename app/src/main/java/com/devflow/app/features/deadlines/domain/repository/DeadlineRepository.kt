package com.devflow.app.features.deadlines.domain.repository

import com.devflow.app.features.deadlines.domain.model.Deadline
import kotlinx.coroutines.flow.Flow

interface DeadlineRepository {

    suspend fun createDeadline(deadline: Deadline): Long

    suspend fun updateDeadline(deadline: Deadline)

    suspend fun deleteDeadline(deadline: Deadline)

    suspend fun getDeadline(id: Long): Deadline?

    fun getAllDeadlines(projectId: Long): Flow<List<Deadline>>

    fun getAllDeadlines(): Flow<List<Deadline>> = kotlinx.coroutines.flow.flowOf(emptyList())
}
