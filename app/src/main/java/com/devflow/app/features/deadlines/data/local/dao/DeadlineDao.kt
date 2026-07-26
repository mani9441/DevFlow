package com.devflow.app.features.deadlines.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devflow.app.features.deadlines.data.local.entity.DeadlineEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface DeadlineDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(deadline: DeadlineEntity): Long

    @Update
    suspend fun update(deadline: DeadlineEntity)

    @Delete
    suspend fun delete(deadline: DeadlineEntity)

    @Query("SELECT * FROM deadlines WHERE id = :id")
    suspend fun getById(id: Long): DeadlineEntity?

    @Query("SELECT * FROM deadlines WHERE projectId = :projectId ORDER BY dueDate ASC")
    fun getAll(projectId: Long): Flow<List<DeadlineEntity>>

    @Query("SELECT * FROM deadlines ORDER BY dueDate ASC")
    fun getAllDeadlines(): Flow<List<DeadlineEntity>>
}
