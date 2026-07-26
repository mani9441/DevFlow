package com.devflow.app.features.meetings.data.repository

import com.devflow.app.features.meetings.data.local.dao.MeetingDao
import com.devflow.app.features.meetings.data.mapper.toDomain
import com.devflow.app.features.meetings.data.mapper.toEntity
import com.devflow.app.features.meetings.domain.model.Meeting
import com.devflow.app.features.meetings.domain.repository.MeetingRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class MeetingRepositoryImpl @Inject constructor(
    private val dao: MeetingDao
) : MeetingRepository {

    override suspend fun createMeeting(meeting: Meeting): Long {
        require(meeting.title.isNotBlank()) { "Title cannot be empty" }
        require(meeting.yesterdayWork.isNotBlank()) { "Yesterday's work cannot be empty" }
        require(meeting.todayPlan.isNotBlank()) { "Today's plan cannot be empty" }

        val newMeeting = meeting.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return dao.insert(newMeeting.toEntity())
    }

    override suspend fun updateMeeting(meeting: Meeting) {
        require(meeting.title.isNotBlank()) { "Title cannot be empty" }
        require(meeting.yesterdayWork.isNotBlank()) { "Yesterday's work cannot be empty" }
        require(meeting.todayPlan.isNotBlank()) { "Today's plan cannot be empty" }

        val updatedMeeting = meeting.copy(
            updatedAt = LocalDateTime.now()
        )
        dao.update(updatedMeeting.toEntity())
    }

    override suspend fun deleteMeeting(meeting: Meeting) {
        dao.delete(meeting.toEntity())
    }

    override suspend fun getMeeting(id: Long): Meeting? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAllMeetings(projectId: Long): Flow<List<Meeting>> {
        return dao.getAll(projectId).map { entities ->
            entities.map { it.toDomain() }
        }
    }

    override fun getAllMeetings(): Flow<List<Meeting>> {
        return dao.getAllMeetings().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
