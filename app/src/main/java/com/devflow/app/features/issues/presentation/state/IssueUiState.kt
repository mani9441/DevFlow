package com.devflow.app.features.issues.presentation.state

import com.devflow.app.features.issues.domain.model.Issue

data class IssueUiState(
    val issueList: List<Issue> = emptyList(),
    val selectedIssue: Issue? = null,
    val loadingState: Boolean = false,
    val errorMessage: String? = null
)
