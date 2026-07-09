package com.devflow.app.features.communication.presentation.state

import com.devflow.app.features.communication.domain.model.TeamMember
import com.devflow.app.features.communication.domain.model.Message

data class CommunicationUiState(
    val membersList: List<TeamMember> = emptyList(),
    val selectedMember: TeamMember? = null,
    val conversationMessages: List<Message> = emptyList(),
    val loadingState: Boolean = false,
    val errorMessage: String? = null
)
