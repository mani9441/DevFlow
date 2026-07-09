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
import com.devflow.app.features.todo.data.local.converter.TodoConverters
import com.devflow.app.features.todo.data.local.dao.TodoDao
import com.devflow.app.features.todo.data.local.entity.TodoEntity

@Database(
    entities = [
        TodoEntity::class,
        NoteEntity::class,
        MeetingEntity::class,
        DeadlineEntity::class
    ],
    version = 3,
    exportSchema = false
)
@TypeConverters(TodoConverters::class)
abstract class DevFlowDatabase : RoomDatabase() {

    abstract fun todoDao(): TodoDao

    abstract fun noteDao(): NoteDao

    abstract fun meetingDao(): MeetingDao

    abstract fun deadlineDao(): DeadlineDao
}