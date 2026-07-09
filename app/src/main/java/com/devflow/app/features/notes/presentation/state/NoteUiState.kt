package com.devflow.app.features.notes.presentation.state

import com.devflow.app.features.notes.domain.model.Note

/**
 * UI State for the Notes feature.
 */
data class NoteUiState(
    val noteList: List<Note> = emptyList(),
    val selectedNote: Note? = null,
    val loadingState: Boolean = false,
    val errorMessage: String? = null
)
