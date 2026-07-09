package com.devflow.app.features.stories.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devflow.app.features.stories.data.local.entity.UserStoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface UserStoryDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(story: UserStoryEntity): Long

    @Update
    suspend fun update(story: UserStoryEntity)

    @Delete
    suspend fun delete(story: UserStoryEntity)

    @Query("SELECT * FROM user_stories WHERE id = :id")
    suspend fun getById(id: Long): UserStoryEntity?

    @Query("SELECT * FROM user_stories ORDER BY createdAt DESC")
    fun getAll(): Flow<List<UserStoryEntity>>

    @Query("SELECT * FROM user_stories WHERE sprintId = :sprintId ORDER BY createdAt DESC")
    fun getBySprint(sprintId: Long): Flow<List<UserStoryEntity>>

    @Query("SELECT * FROM user_stories WHERE sprintId IS NULL ORDER BY createdAt DESC")
    fun getUnassigned(): Flow<List<UserStoryEntity>>
}
