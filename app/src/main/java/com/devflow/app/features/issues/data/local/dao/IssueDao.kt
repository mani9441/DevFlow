package com.devflow.app.features.issues.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devflow.app.features.issues.data.local.entity.IssueEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface IssueDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(issue: IssueEntity): Long

    @Update
    suspend fun update(issue: IssueEntity)

    @Delete
    suspend fun delete(issue: IssueEntity)

    @Query("SELECT * FROM issues WHERE id = :id")
    suspend fun getById(id: Long): IssueEntity?

    @Query("SELECT * FROM issues ORDER BY createdAt DESC")
    fun getAll(): Flow<List<IssueEntity>>
}
