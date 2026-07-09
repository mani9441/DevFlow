package com.devflow.app.features.tasks.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devflow.app.features.tasks.data.local.entity.TaskEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(task: TaskEntity): Long

    @Update
    suspend fun update(task: TaskEntity)

    @Delete
    suspend fun delete(task: TaskEntity)

    @Query("SELECT * FROM tasks WHERE id = :id")
    suspend fun getById(id: Long): TaskEntity?

    @Query("SELECT * FROM tasks ORDER BY createdAt DESC")
    fun getAll(): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE sprintId = :sprintId ORDER BY createdAt DESC")
    fun getBySprint(sprintId: Long): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE sprintId IS NULL ORDER BY createdAt DESC")
    fun getUnassigned(): Flow<List<TaskEntity>>
}
