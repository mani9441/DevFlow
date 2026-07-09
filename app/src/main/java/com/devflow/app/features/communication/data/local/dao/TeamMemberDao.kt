package com.devflow.app.features.communication.data.local.dao

import androidx.room.Dao
import androidx.room.Query
import com.devflow.app.features.communication.data.local.entity.TeamMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamMemberDao {

    @Query("SELECT * FROM team_members ORDER BY id ASC")
    fun getAll(): Flow<List<TeamMemberEntity>>

    @Query("SELECT * FROM team_members WHERE id = :id")
    suspend fun getById(id: Long): TeamMemberEntity?
}
