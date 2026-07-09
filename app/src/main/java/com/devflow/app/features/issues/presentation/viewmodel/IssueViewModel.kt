package com.devflow.app.features.issues.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.issues.domain.model.IssueStatus
import com.devflow.app.features.issues.domain.repository.IssueRepository
import com.devflow.app.features.issues.presentation.state.IssueUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class IssueViewModel @Inject constructor(
    private val repository: IssueRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(IssueUiState())
    val uiState: StateFlow<IssueUiState> = _uiState.asStateFlow()

    init {
        loadIssues()
    }

    fun loadIssues() {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                repository.getAllIssues().collect { issues ->
                    _uiState.update {
                        it.copy(
                            issueList = issues,
                            loadingState = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingState = false,
                        errorMessage = e.message ?: "Failed to load issues"
                    )
                }
            }
        }
    }

    fun loadIssue(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingState = true) }
            val issue = repository.getIssue(id)
            _uiState.update {
                it.copy(
                    selectedIssue = issue,
                    loadingState = false,
                    errorMessage = if (issue == null) "Issue not found" else null
                )
            }
        }
    }

    fun selectIssue(issue: Issue) {
        _uiState.update { it.copy(selectedIssue = issue) }
    }

    fun addIssue(
        title: String,
        description: String?,
        priority: IssuePriority,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val newIssue = Issue(
                    title = title.trim(),
                    description = description?.trim(),
                    priority = priority,
                    status = IssueStatus.OPEN,
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                repository.createIssue(newIssue)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to log issue") }
            }
        }
    }

    fun editIssue(
        issue: Issue,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.updateIssue(issue)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update issue") }
            }
        }
    }

    fun changeStatus(
        issue: Issue,
        status: IssueStatus,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                val updated = issue.copy(status = status)
                repository.updateIssue(updated)
                if (_uiState.value.selectedIssue?.id == issue.id) {
                    _uiState.update { it.copy(selectedIssue = updated) }
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update status") }
            }
        }
    }

    fun deleteIssue(
        issue: Issue,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteIssue(issue)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to delete issue") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
