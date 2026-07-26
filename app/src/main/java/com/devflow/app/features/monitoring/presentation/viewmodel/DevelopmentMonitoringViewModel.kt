package com.devflow.app.features.monitoring.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.monitoring.domain.model.WorkflowRun
import com.devflow.app.features.monitoring.domain.model.RepositoryConfig
import com.devflow.app.features.monitoring.domain.repository.DevelopmentMonitoringRepository
import com.devflow.app.features.monitoring.presentation.state.MonitoringUiState
import com.devflow.app.features.project.domain.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class DevelopmentMonitoringViewModel @Inject constructor(
    private val repository: DevelopmentMonitoringRepository,
    private val projectRepository: ProjectRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MonitoringUiState())
    val uiState: StateFlow<MonitoringUiState> = _uiState.asStateFlow()

    init {
        loadRepository()
    }

    fun loadRepository() {
        _uiState.update { it.copy(loadingConfig = true) }
        viewModelScope.launch {
            try {
                projectRepository.getActiveProject().collect { project ->
                    if (project != null) {
                        repository.getRepository(project.id).collect { config ->
                            _uiState.update {
                                it.copy(
                                    config = config,
                                    loadingConfig = false,
                                    errorMessage = null
                                )
                            }
                            if (config != null) {
                                loadBuilds(config.owner, config.repository, config.personalAccessToken)
                            } else {
                                _uiState.update { it.copy(workflowRuns = emptyList()) }
                            }
                        }
                    } else {
                        _uiState.update {
                            it.copy(
                                config = null,
                                workflowRuns = emptyList(),
                                loadingConfig = false,
                                errorMessage = "No active project selected"
                            )
                        }
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingConfig = false,
                        errorMessage = e.message ?: "Failed to load repository config"
                    )
                }
            }
        }
    }

    fun connectRepository(
        owner: String,
        repoName: String,
        token: String?,
        onSuccess: () -> Unit = {}
    ) {
        if (owner.isBlank() || repoName.isBlank()) {
            _uiState.update { it.copy(errorMessage = "Owner and Repository Name cannot be blank") }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(loadingBuilds = true) }
            try {
                val project = projectRepository.getActiveProject().first()
                if (project != null) {
                    val currentConfig = _uiState.value.config
                    val newConfig = RepositoryConfig(
                        id = currentConfig?.id ?: 0L,
                        projectId = project.id,
                        owner = owner.trim(),
                        repository = repoName.trim(),
                        personalAccessToken = token?.trim()?.ifBlank { null },
                        createdAt = currentConfig?.createdAt ?: LocalDateTime.now(),
                        updatedAt = LocalDateTime.now()
                    )

                    // Verify repo exists and token is valid by making a test API call
                    repository.loadBuildStatus(newConfig.owner, newConfig.repository, newConfig.personalAccessToken)

                    repository.saveRepository(newConfig)
                    _uiState.update { it.copy(errorMessage = null) }
                    onSuccess()
                } else {
                    _uiState.update {
                        it.copy(
                            loadingBuilds = false,
                            errorMessage = "No active project selected"
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingBuilds = false,
                        errorMessage = "Connection test failed: ${e.message}"
                    )
                }
            }
        }
    }

    fun loadBuilds(owner: String, repo: String, token: String?) {
        _uiState.update { it.copy(loadingBuilds = true) }
        viewModelScope.launch {
            try {
                val runs = repository.loadBuildStatus(owner, repo, token)
                _uiState.update {
                    it.copy(
                        workflowRuns = runs,
                        loadingBuilds = false,
                        errorMessage = null
                    )
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingBuilds = false,
                        errorMessage = e.message ?: "Failed to fetch build status runs"
                    )
                }
            }
        }
    }

    fun refreshBuilds() {
        val config = _uiState.value.config ?: return
        loadBuilds(config.owner, config.repository, config.personalAccessToken)
    }

    fun removeRepository(config: RepositoryConfig, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            try {
                repository.deleteRepository(config)
                _uiState.update {
                    it.copy(
                        config = null,
                        workflowRuns = emptyList(),
                        selectedRun = null,
                        errorMessage = null
                    )
                }
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to remove repository config") }
            }
        }
    }

    fun selectRun(run: WorkflowRun?) {
        _uiState.update { it.copy(selectedRun = run) }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
