package com.devflow.app.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.devflow.app.features.deadlines.data.local.dao.DeadlineDao
import com.devflow.app.features.deadlines.data.local.entity.DeadlineEntity
import com.devflow.app.features.meetings.data.local.dao.MeetingDao
import com.devflow.app.features.meetings.data.local.entity.MeetingEntity
import com.devflow.app.features.notes.data.local.dao.NoteDao
import com.devflow.app.features.notes.data.local.entity.NoteEntity
import com.devflow.app.features.sprint.data.local.dao.SprintDao
import com.devflow.app.features.sprint.data.local.entity.SprintEntity
import com.devflow.app.features.tasks.data.local.dao.TaskDao
import com.devflow.app.features.tasks.data.local.entity.TaskEntity
import com.devflow.app.features.stories.data.local.dao.UserStoryDao
import com.devflow.app.features.stories.data.local.entity.UserStoryEntity
import com.devflow.app.features.communication.data.local.dao.MessageDao
import com.devflow.app.features.communication.data.local.entity.MessageEntity
import com.devflow.app.features.communication.data.local.dao.TeamMemberDao
import com.devflow.app.features.communication.data.local.entity.TeamMemberEntity
import com.devflow.app.features.issues.data.local.dao.IssueDao
import com.devflow.app.features.issues.data.local.entity.IssueEntity
import com.devflow.app.features.monitoring.data.local.dao.RepositoryConfigDao
import com.devflow.app.features.monitoring.data.local.entity.RepositoryConfigEntity
import com.devflow.app.features.todo.data.local.converter.TodoConverters
import com.devflow.app.features.todo.data.local.dao.TodoDao
import com.devflow.app.features.todo.data.local.entity.TodoEntity
import com.devflow.app.features.project.data.local.dao.ProjectDao
import com.devflow.app.features.project.data.local.entity.ProjectEntity

@Database(
    entities = [
        ProjectEntity::class,
        TodoEntity::class,
        NoteEntity::class,
        MeetingEntity::class,
        DeadlineEntity::class,
        SprintEntity::class,
        TaskEntity::class,
        UserStoryEntity::class,
        TeamMemberEntity::class,
        MessageEntity::class,
        IssueEntity::class,
        RepositoryConfigEntity::class
    ],
    version = 7,
    exportSchema = false
)
@TypeConverters(TodoConverters::class)
abstract class DevFlowDatabase : RoomDatabase() {

    abstract fun projectDao(): ProjectDao

    abstract fun todoDao(): TodoDao

    abstract fun noteDao(): NoteDao

    abstract fun meetingDao(): MeetingDao

    abstract fun deadlineDao(): DeadlineDao

    abstract fun sprintDao(): SprintDao

    abstract fun taskDao(): TaskDao

    abstract fun userStoryDao(): UserStoryDao

    abstract fun teamMemberDao(): TeamMemberDao

    abstract fun messageDao(): MessageDao

    abstract fun issueDao(): IssueDao

    abstract fun repositoryConfigDao(): RepositoryConfigDao
}