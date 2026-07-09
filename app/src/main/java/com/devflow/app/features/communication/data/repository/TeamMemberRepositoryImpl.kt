package com.devflow.app.features.communication.data.repository

import com.devflow.app.features.communication.data.local.dao.TeamMemberDao
import com.devflow.app.features.communication.data.mapper.toDomain
import com.devflow.app.features.communication.domain.model.TeamMember
import com.devflow.app.features.communication.domain.repository.TeamMemberRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TeamMemberRepositoryImpl @Inject constructor(
    private val dao: TeamMemberDao
) : TeamMemberRepository {

    override fun getAllMembers(): Flow<List<TeamMember>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override suspend fun getMember(id: Long): TeamMember? {
        return dao.getById(id)?.toDomain()
    }
}
