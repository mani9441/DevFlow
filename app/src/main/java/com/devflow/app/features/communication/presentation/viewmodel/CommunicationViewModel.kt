package com.devflow.app.features.communication.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.communication.domain.model.Message
import com.devflow.app.features.communication.domain.model.TeamMember
import com.devflow.app.features.communication.domain.repository.MessageRepository
import com.devflow.app.features.communication.domain.repository.TeamMemberRepository
import com.devflow.app.features.communication.presentation.state.CommunicationUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class CommunicationViewModel @Inject constructor(
    private val memberRepository: TeamMemberRepository,
    private val messageRepository: MessageRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(CommunicationUiState())
    val uiState: StateFlow<CommunicationUiState> = _uiState.asStateFlow()

    // Default current user is Marcus (id = 1L)
    val currentUserId = 1L

    init {
        loadTeamMembers()
    }

    fun loadTeamMembers() {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                memberRepository.getAllMembers().collect { members ->
                    // Exclude current user (Marcus) from chat list
                    val otherMembers = members.filter { it.id != currentUserId }
                    _uiState.update {
                        it.copy(
                            membersList = otherMembers,
                            loadingState = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingState = false,
                        errorMessage = e.message ?: "Failed to load team members"
                    )
                }
            }
        }
    }

    fun loadConversation(receiverId: Long) {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                val member = memberRepository.getMember(receiverId)
                _uiState.update { it.copy(selectedMember = member) }

                messageRepository.getConversation(currentUserId, receiverId).collect { messages ->
                    _uiState.update {
                        it.copy(
                            conversationMessages = messages,
                            loadingState = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingState = false,
                        errorMessage = e.message ?: "Failed to load conversation"
                    )
                }
            }
        }
    }

    fun sendMessage(messageText: String, onSuccess: () -> Unit = {}) {
        val receiver = _uiState.value.selectedMember ?: return
        if (messageText.isBlank()) return

        viewModelScope.launch {
            try {
                val message = Message(
                    senderId = currentUserId,
                    receiverId = receiver.id,
                    message = messageText.trim(),
                    sentAt = LocalDateTime.now()
                )
                messageRepository.sendMessage(message)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to send message") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
