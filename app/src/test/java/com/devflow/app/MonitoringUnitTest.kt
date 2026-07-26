package com.devflow.app

import com.devflow.app.features.monitoring.data.local.entity.RepositoryConfigEntity
import com.devflow.app.features.monitoring.data.mapper.toDomain
import com.devflow.app.features.monitoring.data.mapper.toEntity
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class MonitoringUnitTest {

    @Test
    fun repositoryConfigMapping_isCorrect() {
        val now = LocalDateTime.now()
        val entity = RepositoryConfigEntity(
            id = 5L,
            projectId = 1L,
            owner = "openai",
            repository = "openai-python",
            personalAccessToken = "ghp_mock_token_12345",
            createdAt = now,
            updatedAt = now
        )

        val domain = entity.toDomain()

        assertEquals(5L, domain.id)
        assertEquals(1L, domain.projectId)
        assertEquals("openai", domain.owner)
        assertEquals("openai-python", domain.repository)
        assertEquals("ghp_mock_token_12345", domain.personalAccessToken)
        assertEquals(now, domain.createdAt)
        assertEquals(now, domain.updatedAt)

        val entityFromDomain = domain.toEntity()

        assertEquals(entity, entityFromDomain)
    }

    @Test
    fun repositoryConfigMapping_nullToken_isCorrect() {
        val now = LocalDateTime.now()
        val entity = RepositoryConfigEntity(
            id = 12L,
            projectId = 1L,
            owner = "tensorflow",
            repository = "tensorflow",
            personalAccessToken = null,
            createdAt = now,
            updatedAt = now
        )

        val domain = entity.toDomain()

        assertEquals(12L, domain.id)
        assertEquals(1L, domain.projectId)
        assertEquals("tensorflow", domain.owner)
        assertEquals("tensorflow", domain.repository)
        assertEquals(null, domain.personalAccessToken)
        assertEquals(now, domain.createdAt)
        assertEquals(now, domain.updatedAt)

        val entityFromDomain = domain.toEntity()

        assertEquals(entity, entityFromDomain)
    }

    @Test
    fun jsonParsing_toWorkflowRun_isCorrect() {
        val mockJson = """
            {
              "workflow_runs": [
                {
                  "id": 18245,
                  "name": "CI",
                  "status": "completed",
                  "conclusion": "success",
                  "head_branch": "master",
                  "run_number": 52,
                  "event": "push",
                  "created_at": "2026-07-09T09:45:00Z",
                  "updated_at": "2026-07-09T09:51:00Z",
                  "html_url": "https://github.com/openai/openai-python/actions/runs/18245"
                }
              ]
            }
        """.trimIndent()

        val jsonObject = org.json.JSONObject(mockJson)
        val runsArray = jsonObject.getJSONArray("workflow_runs")
        val runObject = runsArray.getJSONObject(0)

        val id = runObject.getLong("id")
        val name = runObject.getString("name")
        val status = runObject.getString("status")
        val conclusion = runObject.getString("conclusion")
        val branch = runObject.getString("head_branch")
        val runNumber = runObject.getInt("run_number")
        val event = runObject.getString("event")
        val createdAt = runObject.getString("created_at")
        val updatedAt = runObject.getString("updated_at")
        val htmlUrl = runObject.getString("html_url")

        val run = com.devflow.app.features.monitoring.domain.model.WorkflowRun(
            id = id,
            workflowName = name,
            status = status,
            conclusion = conclusion,
            branch = branch,
            runNumber = runNumber,
            event = event,
            createdAt = createdAt,
            updatedAt = updatedAt,
            url = htmlUrl
        )

        assertEquals(18245L, run.id)
        assertEquals("CI", run.workflowName)
        assertEquals("completed", run.status)
        assertEquals("success", run.conclusion)
        assertEquals("master", run.branch)
        assertEquals(52, run.runNumber)
        assertEquals("push", run.event)
        assertEquals("2026-07-09T09:45:00Z", run.createdAt)
        assertEquals("2026-07-09T09:51:00Z", run.updatedAt)
        assertEquals("https://github.com/openai/openai-python/actions/runs/18245", run.url)
    }
}
