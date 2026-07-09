package com.devflow.app.features.progress.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.progress.domain.model.SprintProgress
import com.devflow.app.features.progress.domain.repository.SprintProgressRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SprintProgressViewModel @Inject constructor(
    private val repository: SprintProgressRepository
) : ViewModel() {

    private val _progress = MutableStateFlow<SprintProgress?>(null)
    val progress: StateFlow<SprintProgress?> = _progress.asStateFlow()

    fun loadProgress(sprintId: Long) {
        viewModelScope.launch {
            repository.getProgressForSprint(sprintId).collectLatest { progressData ->
                _progress.value = progressData
            }
        }
    }
}
