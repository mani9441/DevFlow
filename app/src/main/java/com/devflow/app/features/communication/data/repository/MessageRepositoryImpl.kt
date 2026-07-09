package com.devflow.app.features.communication.data.repository

import com.devflow.app.features.communication.data.local.dao.MessageDao
import com.devflow.app.features.communication.data.mapper.toDomain
import com.devflow.app.features.communication.data.mapper.toEntity
import com.devflow.app.features.communication.domain.model.Message
import com.devflow.app.features.communication.domain.repository.MessageRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class MessageRepositoryImpl @Inject constructor(
    private val dao: MessageDao
) : MessageRepository {

    override suspend fun sendMessage(message: Message): Long {
        require(message.message.isNotBlank()) { "Message content cannot be blank" }

        val newMessage = message.copy(
            sentAt = LocalDateTime.now()
        )
        return dao.insert(newMessage.toEntity())
    }

    override fun getConversation(senderId: Long, receiverId: Long): Flow<List<Message>> {
        return dao.getConversation(senderId, receiverId).map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
