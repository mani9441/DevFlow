package com.devflow.app.features.meetings.domain.repository

import com.devflow.app.features.meetings.domain.model.Meeting
import kotlinx.coroutines.flow.Flow

interface MeetingRepository {

    suspend fun createMeeting(meeting: Meeting): Long

    suspend fun updateMeeting(meeting: Meeting)

    suspend fun deleteMeeting(meeting: Meeting)

    suspend fun getMeeting(id: Long): Meeting?

    fun getAllMeetings(projectId: Long): Flow<List<Meeting>>

    fun getAllMeetings(): Flow<List<Meeting>> = kotlinx.coroutines.flow.flowOf(emptyList())
}
