package com.devflow.app.features.communication.domain.repository

import com.devflow.app.features.communication.domain.model.TeamMember
import kotlinx.coroutines.flow.Flow

interface TeamMemberRepository {

    fun getAllMembers(): Flow<List<TeamMember>>

    suspend fun getMember(id: Long): TeamMember?
}
