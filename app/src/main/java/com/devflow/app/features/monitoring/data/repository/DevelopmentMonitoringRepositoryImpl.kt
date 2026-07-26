package com.devflow.app.features.monitoring.data.repository

import com.devflow.app.features.monitoring.data.local.dao.RepositoryConfigDao
import com.devflow.app.features.monitoring.data.mapper.toDomain
import com.devflow.app.features.monitoring.data.mapper.toEntity
import com.devflow.app.features.monitoring.domain.model.WorkflowRun
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import com.devflow.app.features.monitoring.domain.repository.DevelopmentMonitoringRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDateTime
import javax.inject.Inject

class DevelopmentMonitoringRepositoryImpl @Inject constructor(
    private val dao: RepositoryConfigDao
) : DevelopmentMonitoringRepository {

    override suspend fun saveRepository(config: RepositoryConfig): Long = withContext(Dispatchers.IO) {
        val now = LocalDateTime.now()
        val toSave = config.copy(
            createdAt = if (config.id == 0L) now else config.createdAt,
            updatedAt = now
        )
        dao.insert(toSave.toEntity())
    }

    override fun getRepository(projectId: Long): Flow<RepositoryConfig?> {
        return dao.getConfig(projectId).map { entity ->
            entity?.toDomain()
        }
    }

    override fun getAllRepositoryConfigs(): Flow<List<RepositoryConfig>> {
        return dao.getAllConfigs().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun deleteRepository(config: RepositoryConfig) = withContext(Dispatchers.IO) {
        dao.delete(config.toEntity())
    }

    override suspend fun loadBuildStatus(
        owner: String,
        repo: String,
        token: String?
    ): List<WorkflowRun> = withContext(Dispatchers.IO) {
        val urlStr = "https://api.github.com/repos/$owner/$repo/actions/runs"
        val url = URL(urlStr)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.connectTimeout = 8000
        connection.readTimeout = 8000
        connection.setRequestProperty("Accept", "application/vnd.github+json")
        connection.setRequestProperty("User-Agent", "DevFlow-Monitoring")

        if (!token.isNullOrBlank()) {
            connection.setRequestProperty("Authorization", "Bearer $token")
        }

        val responseCode = connection.responseCode
        if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }
            reader.close()

            val jsonObject = JSONObject(response.toString())
            val runsArray = jsonObject.optJSONArray("workflow_runs") ?: return@withContext emptyList<WorkflowRun>()
            val list = mutableListOf<WorkflowRun>()

            for (i in 0 until runsArray.length()) {
                val run = runsArray.getJSONObject(i)
                val id = run.optLong("id", 0L)
                val name = run.optString("name", "Unknown Workflow")
                val status = run.optString("status", "unknown")
                val conclusion = run.optString("conclusion", "pending")
                val branch = run.optString("head_branch", "unknown")
                val runNumber = run.optInt("run_number", 0)
                val event = run.optString("event", "")
                val createdAtStr = run.optString("created_at", "")
                val updatedAtStr = run.optString("updated_at", "")
                val htmlUrl = run.optString("html_url", "")

                list.add(
                    WorkflowRun(
                        id = id,
                        workflowName = name,
                        status = status,
                        conclusion = conclusion,
                        branch = branch,
                        runNumber = runNumber,
                        event = event,
                        createdAt = createdAtStr,
                        updatedAt = updatedAtStr,
                        url = htmlUrl
                    )
                )
            }
            list
        } else {
            val errorReader = BufferedReader(InputStreamReader(connection.errorStream ?: connection.inputStream))
            val errorResponse = StringBuilder()
            var line: String?
            while (errorReader.readLine().also { line = it } != null) {
                errorResponse.append(line)
            }
            errorReader.close()
            throw Exception("GitHub API Error (HTTP $responseCode): ${errorResponse.toString()}")
        }
    }
}
