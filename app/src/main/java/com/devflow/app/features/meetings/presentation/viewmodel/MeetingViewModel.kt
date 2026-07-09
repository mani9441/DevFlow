package com.devflow.app.features.meetings.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.meetings.domain.model.Meeting
import com.devflow.app.features.meetings.domain.repository.MeetingRepository
import com.devflow.app.features.meetings.presentation.state.MeetingUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class MeetingViewModel @Inject constructor(
    private val repository: MeetingRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeetingUiState())
    val uiState: StateFlow<MeetingUiState> = _uiState.asStateFlow()

    init {
        loadMeetings()
    }

    fun loadMeetings() {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                repository.getAllMeetings().collect { meetings ->
                    _uiState.update {
                        it.copy(
                            meetingList = meetings,
                            loadingState = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingState = false,
                        errorMessage = e.message ?: "Failed to load meetings"
                    )
                }
            }
        }
    }

    fun loadMeeting(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingState = true) }
            val meeting = repository.getMeeting(id)
            _uiState.update {
                it.copy(
                    selectedMeeting = meeting,
                    loadingState = false,
                    errorMessage = if (meeting == null) "Meeting not found" else null
                )
            }
        }
    }

    fun selectMeeting(meeting: Meeting) {
        _uiState.update {
            it.copy(selectedMeeting = meeting)
        }
    }

    fun addMeeting(
        title: String,
        meetingDate: LocalDate,
        meetingTime: String,
        yesterdayWork: String,
        todayPlan: String,
        blockers: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val newMeeting = Meeting(
                    title = title.trim(),
                    meetingDate = meetingDate,
                    meetingTime = meetingTime.trim(),
                    yesterdayWork = yesterdayWork.trim(),
                    todayPlan = todayPlan.trim(),
                    blockers = blockers.trim(),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                repository.createMeeting(newMeeting)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to add meeting") }
            }
        }
    }

    fun editMeeting(
        meeting: Meeting,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.updateMeeting(meeting)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update meeting") }
            }
        }
    }

    fun deleteMeeting(
        meeting: Meeting,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteMeeting(meeting)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to delete meeting") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
