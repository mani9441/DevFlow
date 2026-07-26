package com.devflow.app.features.communication.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.devflow.app.features.communication.data.local.entity.TeamMemberEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamMemberDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(teamMember: TeamMemberEntity): Long

    @Query("SELECT * FROM team_members WHERE projectId = :projectId ORDER BY id ASC")
    fun getAll(projectId: Long): Flow<List<TeamMemberEntity>>

    @Query("SELECT * FROM team_members WHERE id = :id")
    suspend fun getById(id: Long): TeamMemberEntity?
}
