package com.devflow.app.features.sprint.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devflow.app.features.sprint.data.local.entity.SprintEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SprintDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(sprint: SprintEntity): Long

    @Update
    suspend fun update(sprint: SprintEntity)

    @Delete
    suspend fun delete(sprint: SprintEntity)

    @Query("SELECT * FROM sprints WHERE id = :id")
    suspend fun getById(id: Long): SprintEntity?

    @Query("SELECT * FROM sprints WHERE projectId = :projectId ORDER BY startDate DESC")
    fun getAll(projectId: Long): Flow<List<SprintEntity>>

    @Query("SELECT * FROM sprints ORDER BY startDate DESC")
    fun getAllSprints(): Flow<List<SprintEntity>>
}
