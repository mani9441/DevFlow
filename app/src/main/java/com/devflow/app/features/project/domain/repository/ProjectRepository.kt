package com.devflow.app.features.project.domain.repository

import com.devflow.app.features.project.domain.model.Project
import kotlinx.coroutines.flow.Flow

interface ProjectRepository {
    fun getAllProjects(): Flow<List<Project>>
    suspend fun getProject(id: Long): Project?
    suspend fun createProject(project: Project): Long
    suspend fun updateProject(project: Project)
    suspend fun deleteProject(project: Project)
    suspend fun archiveProject(id: Long)
    fun getActiveProject(): Flow<Project?>
    suspend fun setActiveProject(projectId: Long?)
}
