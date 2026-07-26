package com.devflow.app.features.tasks.domain.repository

import com.devflow.app.features.tasks.domain.model.Task
import kotlinx.coroutines.flow.Flow

interface TaskRepository {

    suspend fun createTask(task: Task): Long

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun getTask(id: Long): Task?

    fun getAllTasks(projectId: Long): Flow<List<Task>>

    fun getAllTasks(): Flow<List<Task>> = kotlinx.coroutines.flow.flowOf(emptyList())

    fun getTasksBySprint(sprintId: Long): Flow<List<Task>>

    fun getUnassignedTasks(projectId: Long): Flow<List<Task>>
}
