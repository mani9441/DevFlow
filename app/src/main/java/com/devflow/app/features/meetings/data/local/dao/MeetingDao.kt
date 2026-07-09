package com.devflow.app.features.meetings.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.devflow.app.features.meetings.data.local.entity.MeetingEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface MeetingDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(meeting: MeetingEntity): Long

    @Update
    suspend fun update(meeting: MeetingEntity)

    @Delete
    suspend fun delete(meeting: MeetingEntity)

    @Query("SELECT * FROM meetings WHERE id = :id")
    suspend fun getById(id: Long): MeetingEntity?

    @Query("SELECT * FROM meetings ORDER BY meetingDate DESC, meetingTime DESC")
    fun getAll(): Flow<List<MeetingEntity>>
}
