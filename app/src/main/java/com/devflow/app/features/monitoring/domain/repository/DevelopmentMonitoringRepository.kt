package com.devflow.app.features.monitoring.domain.repository

import com.devflow.app.features.monitoring.domain.model.WorkflowRun
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import kotlinx.coroutines.flow.Flow

interface DevelopmentMonitoringRepository {

    suspend fun saveRepository(config: RepositoryConfig): Long

    fun getRepository(): Flow<RepositoryConfig?>

    suspend fun deleteRepository(config: RepositoryConfig)

    suspend fun loadBuildStatus(owner: String, repo: String, token: String?): List<WorkflowRun>
}
