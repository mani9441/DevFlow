package com.devflow.app.features.todo.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devflow.app.features.todo.data.local.entity.TodoEntity
import kotlinx.coroutines.flow.Flow
import java.time.LocalDateTime

@Dao
interface TodoDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(todo: TodoEntity): Long

    @Update
    suspend fun update(todo: TodoEntity)

    @Delete
    suspend fun delete(todo: TodoEntity)

    @Query("SELECT * FROM todos WHERE id = :id")
    suspend fun getById(id: Long): TodoEntity?

    @Query("SELECT * FROM todos ORDER BY createdAt DESC")
    fun getAll(): Flow<List<TodoEntity>>

    @Query("UPDATE todos SET status = :status, updatedAt = :updatedAt WHERE id = :id")
    suspend fun updateStatus(
        id: Long,
        status: String,
        updatedAt: LocalDateTime
    )
}