package com.devflow.app.features.issues.data.repository

import com.devflow.app.features.issues.data.local.dao.IssueDao
import com.devflow.app.features.issues.data.mapper.toDomain
import com.devflow.app.features.issues.data.mapper.toEntity
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.issues.domain.repository.IssueRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class IssueRepositoryImpl @Inject constructor(
    private val dao: IssueDao
) : IssueRepository {

    override suspend fun createIssue(issue: Issue): Long {
        require(issue.title.isNotBlank()) { "Issue title cannot be empty" }

        val newIssue = issue.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return dao.insert(newIssue.toEntity())
    }

    override suspend fun updateIssue(issue: Issue) {
        require(issue.title.isNotBlank()) { "Issue title cannot be empty" }

        val updatedIssue = issue.copy(
            updatedAt = LocalDateTime.now()
        )
        dao.update(updatedIssue.toEntity())
    }

    override suspend fun deleteIssue(issue: Issue) {
        dao.delete(issue.toEntity())
    }

    override suspend fun getIssue(id: Long): Issue? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAllIssues(projectId: Long): Flow<List<Issue>> {
        return dao.getAll(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllIssues(): Flow<List<Issue>> {
        return dao.getAllIssues().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
