package com.devflow.app.features.tasks.data.repository

import com.devflow.app.features.tasks.data.local.dao.TaskDao
import com.devflow.app.features.tasks.data.mapper.toDomain
import com.devflow.app.features.tasks.data.mapper.toEntity
import com.devflow.app.features.tasks.domain.model.Task
import com.devflow.app.features.tasks.domain.repository.TaskRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class TaskRepositoryImpl @Inject constructor(
    private val dao: TaskDao
) : TaskRepository {

    override suspend fun createTask(task: Task): Long {
        require(task.title.isNotBlank()) { "Task title cannot be empty" }

        val newTask = task.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return dao.insert(newTask.toEntity())
    }

    override suspend fun updateTask(task: Task) {
        require(task.title.isNotBlank()) { "Task title cannot be empty" }

        val updatedTask = task.copy(
            updatedAt = LocalDateTime.now()
        )
        dao.update(updatedTask.toEntity())
    }

    override suspend fun deleteTask(task: Task) {
        dao.delete(task.toEntity())
    }

    override suspend fun getTask(id: Long): Task? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAllTasks(projectId: Long): Flow<List<Task>> {
        return dao.getAll(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllTasks(): Flow<List<Task>> {
        return dao.getAllTasks().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getTasksBySprint(sprintId: Long): Flow<List<Task>> {
        return dao.getBySprint(sprintId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getUnassignedTasks(projectId: Long): Flow<List<Task>> {
        return dao.getUnassigned(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
