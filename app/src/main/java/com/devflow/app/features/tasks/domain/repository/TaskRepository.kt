package com.devflow.app.features.tasks.domain.repository

import com.devflow.app.features.tasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun createTask(task: Task): Long

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun getTask(id: Long): Task?

    fun getAllTasks(): Flow<List<Task>>

    fun getTasksBySprint(sprintId: Long): Flow<List<Task>>

    fun getUnassignedTasks(): Flow<List<Task>>
}
