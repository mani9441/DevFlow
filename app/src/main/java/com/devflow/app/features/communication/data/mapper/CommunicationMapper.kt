package com.devflow.app.features.communication.data.mapper

import com.devflow.app.features.communication.data.local.entity.MessageEntity
import com.devflow.app.features.communication.data.local.entity.TeamMemberEntity
import com.devflow.app.features.communication.domain.model.Message
import com.devflow.app.features.communication.domain.model.TeamMember

fun TeamMemberEntity.toDomain(): TeamMember {
    return TeamMember(
        id = id,
        name = name,
        role = role
    )
}

fun TeamMember.toEntity(): TeamMemberEntity {
    return TeamMemberEntity(
        id = id,
        name = name,
        role = role
    )
}

fun MessageEntity.toDomain(): Message {
    return Message(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        message = message,
        sentAt = sentAt
    )
}

fun Message.toEntity(): MessageEntity {
    return MessageEntity(
        id = id,
        senderId = senderId,
        receiverId = receiverId,
        message = message,
        sentAt = sentAt
    )
}
