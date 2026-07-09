package com.devflow.app

import com.devflow.app.features.communication.data.local.entity.MessageEntity
import com.devflow.app.features.communication.data.local.entity.TeamMemberEntity
import com.devflow.app.features.communication.data.mapper.toDomain
import com.devflow.app.features.communication.data.mapper.toEntity
import com.devflow.app.features.communication.domain.model.Message
import com.devflow.app.features.communication.domain.model.TeamMember
import com.devflow.app.features.issues.data.local.entity.IssueEntity
import com.devflow.app.features.issues.data.mapper.toDomain
import com.devflow.app.features.issues.data.mapper.toEntity
import com.devflow.app.features.issues.domain.model.Issue
import com.devflow.app.features.issues.domain.model.IssuePriority
import com.devflow.app.features.issues.domain.model.IssueStatus
import org.junit.Assert.assertEquals
import org.junit.Test
import java.time.LocalDateTime

class CollaborationUnitTest {

    @Test
    fun teamMemberMapping_isCorrect() {
        val entity = TeamMemberEntity(
            id = 2L,
            name = "John",
            role = "Backend Developer"
        )

        val domain = entity.toDomain()

        assertEquals(2L, domain.id)
        assertEquals("John", domain.name)
        assertEquals("Backend Developer", domain.role)

        val entityFromDomain = domain.toEntity()

        assertEquals(entity, entityFromDomain)
    }

    @Test
    fun messageMapping_isCorrect() {
        val now = LocalDateTime.now()
        val entity = MessageEntity(
            id = 15L,
            senderId = 1L,
            receiverId = 3L,
            message = "Verify backend tests",
            sentAt = now
        )

        val domain = entity.toDomain()

        assertEquals(15L, domain.id)
        assertEquals(1L, domain.senderId)
        assertEquals(3L, domain.receiverId)
        assertEquals("Verify backend tests", domain.message)
        assertEquals(now, domain.sentAt)

        val entityFromDomain = domain.toEntity()

        assertEquals(entity, entityFromDomain)
    }

    @Test
    fun issueMapping_isCorrect() {
        val now = LocalDateTime.now()
        val entity = IssueEntity(
            id = 101L,
            title = "App crash on login",
            description = "Clicking the login button throws a NullPointerException",
            priority = "CRITICAL",
            status = "IN_PROGRESS",
            createdAt = now,
            updatedAt = now
        )

        val domain = entity.toDomain()

        assertEquals(101L, domain.id)
        assertEquals("App crash on login", domain.title)
        assertEquals("Clicking the login button throws a NullPointerException", domain.description)
        assertEquals(IssuePriority.CRITICAL, domain.priority)
        assertEquals(IssueStatus.IN_PROGRESS, domain.status)
        assertEquals(now, domain.createdAt)
        assertEquals(now, domain.updatedAt)

        val entityFromDomain = domain.toEntity()

        assertEquals(entity, entityFromDomain)
    }
}
