package com.devflow.app.features.progress.data.repository

import com.devflow.app.features.progress.domain.model.SprintProgress
import com.devflow.app.features.progress.domain.repository.SprintProgressRepository
import com.devflow.app.features.tasks.data.local.dao.TaskDao
import com.devflow.app.features.tasks.domain.model.TaskStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SprintProgressRepositoryImpl @Inject constructor(
    private val taskDao: TaskDao
) : SprintProgressRepository {

    override fun getProgressForSprint(sprintId: Long): Flow<SprintProgress> {
        return taskDao.getBySprint(sprintId).map { entities ->
            val total = entities.size
            val completed = entities.count { it.status == TaskStatus.COMPLETED.name }
            val pending = entities.count { it.status == TaskStatus.PENDING.name }
            val inProgress = entities.count { it.status == TaskStatus.IN_PROGRESS.name }
            val percentage = if (total > 0) (completed * 100) / total else 0

            SprintProgress(
                totalTasks = total,
                completedTasks = completed,
                pendingTasks = pending,
                inProgressTasks = inProgress,
                progressPercentage = percentage
            )
        }
    }
}
