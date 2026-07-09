package com.devflow.app.features.meetings.presentation.state

import com.devflow.app.features.meetings.domain.model.Meeting

data class MeetingUiState(
    val meetingList: List<Meeting> = emptyList(),
    val selectedMeeting: Meeting? = null,
    val loadingState: Boolean = false,
    val errorMessage: String? = null
)
