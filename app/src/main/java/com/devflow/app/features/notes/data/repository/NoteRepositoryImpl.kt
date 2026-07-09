package com.devflow.app.features.notes.data.repository

import com.devflow.app.features.notes.data.local.dao.NoteDao
import com.devflow.app.features.notes.data.mapper.toDomain
import com.devflow.app.features.notes.data.mapper.toEntity
import com.devflow.app.features.notes.domain.model.Note
import com.devflow.app.features.notes.domain.repository.NoteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDateTime
import javax.inject.Inject

class NoteRepositoryImpl @Inject constructor(
    private val dao: NoteDao
) : NoteRepository {

    override suspend fun createNote(note: Note): Long {
        require(note.title.isNotBlank()) { "Title cannot be empty" }
        require(note.content.isNotBlank()) { "Content cannot be empty" }

        val newNote = note.copy(
            createdAt = LocalDateTime.now(),
            updatedAt = LocalDateTime.now()
        )
        return dao.insert(newNote.toEntity())
    }

    override suspend fun updateNote(note: Note) {
        require(note.title.isNotBlank()) { "Title cannot be empty" }
        require(note.content.isNotBlank()) { "Content cannot be empty" }

        val updatedNote = note.copy(
            updatedAt = LocalDateTime.now()
        )
        dao.update(updatedNote.toEntity())
    }

    override suspend fun deleteNote(note: Note) {
        dao.delete(note.toEntity())
    }

    override suspend fun getNote(id: Long): Note? {
        return dao.getById(id)?.toDomain()
    }

    override fun getAllNotes(): Flow<List<Note>> {
        return dao.getAll().map { entities ->
            entities.map { it.toDomain() }
        }
    }
}
