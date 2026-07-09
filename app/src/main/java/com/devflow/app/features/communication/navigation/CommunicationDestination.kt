package com.devflow.app.features.communication.navigation

object CommunicationDestination {

    const val MEMBERS_LIST = "communication_members"

    const val CONVERSATION = "communication_conversation/{receiverId}"

    fun conversation(receiverId: Long): String {
        return "communication_conversation/$receiverId"
    }
}
