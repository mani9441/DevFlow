package com.devflow.app.features.issues.domain.repository

import com.devflow.app.features.issues.domain.model.Issue
import kotlinx.coroutines.flow.Flow

interface IssueRepository {

    suspend fun createIssue(issue: Issue): Long

    suspend fun updateIssue(issue: Issue)

    suspend fun deleteIssue(issue: Issue)

    suspend fun getIssue(id: Long): Issue?

    fun getAllIssues(projectId: Long): Flow<List<Issue>>

    fun getAllIssues(): Flow<List<Issue>> = kotlinx.coroutines.flow.flowOf(emptyList())
}
