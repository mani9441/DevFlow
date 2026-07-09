package com.devflow.app.features.communication.domain.repository

import com.devflow.app.features.communication.domain.model.Message
import kotlinx.coroutines.flow.Flow

interface MessageRepository {

    suspend fun sendMessage(message: Message): Long

    fun getConversation(senderId: Long, receiverId: Long): Flow<List<Message>>
}
