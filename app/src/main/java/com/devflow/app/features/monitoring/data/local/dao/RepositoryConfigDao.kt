package com.devflow.app.features.monitoring.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devflow.app.features.monitoring.data.local.entity.RepositoryConfigEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface RepositoryConfigDao {

    @Query("SELECT * FROM repository_configs LIMIT 1")
    fun getConfig(): Flow<RepositoryConfigEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(config: RepositoryConfigEntity): Long

    @Update
    suspend fun update(config: RepositoryConfigEntity)

    @Delete
    suspend fun delete(config: RepositoryConfigEntity)
}
