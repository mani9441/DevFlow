package com.devflow.app.features.notes.data.mapper

import com.devflow.app.features.notes.data.local.entity.NoteEntity
import com.devflow.app.features.notes.domain.model.Note

fun NoteEntity.toDomain(): Note {
    return Note(
        id = id,
        projectId = projectId,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}

fun Note.toEntity(): NoteEntity {
    return NoteEntity(
        id = id,
        projectId = projectId,
        title = title,
        content = content,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}
