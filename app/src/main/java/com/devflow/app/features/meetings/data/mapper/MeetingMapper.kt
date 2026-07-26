package com.devflow.app.features.meetings.data.mapper

import com.devflow.app.features.meetings.data.local.entity.MeetingEntity
import com.devflow.app.features.meetings.domain.model.Meeting

fun MeetingEntity.toDomain(): Meeting {
    return Meeting(
        id = id,
        projectId = projectId,
        title = title,
        meetingDate = meetingDate,
        meetingTime = meetingTime,
        yesterdayWork = yesterdayWork,
        todayPlan = todayPlan,
        blockers = blockers,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Meeting.toEntity(): MeetingEntity {
    return MeetingEntity(
        id = id,
        projectId = projectId,
        title = title,
        meetingDate = meetingDate,
        meetingTime = meetingTime,
        yesterdayWork = yesterdayWork,
        todayPlan = todayPlan,
        blockers = blockers,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
