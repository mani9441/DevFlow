package com.devflow.app.features.notes.domain.repository

import com.devflow.app.features.notes.domain.model.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {

    suspend fun createNote(note: Note): Long

    suspend fun updateNote(note: Note)

    suspend fun deleteNote(note: Note)

    suspend fun getNote(id: Long): Note?

    fun getAllNotes(): Flow<List<Note>>
}
