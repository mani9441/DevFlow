package com.devflow.app.features.notes.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.devflow.app.features.notes.domain.model.Note
import com.devflow.app.features.notes.domain.repository.NoteRepository
import com.devflow.app.features.notes.presentation.state.NoteUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

@HiltViewModel
class NoteViewModel @Inject constructor(
    private val repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(NoteUiState())
    val uiState: StateFlow<NoteUiState> = _uiState.asStateFlow()

    init {
        loadNotes()
    }

    fun loadNotes() {
        _uiState.update { it.copy(loadingState = true) }
        viewModelScope.launch {
            try {
                repository.getAllNotes().collect { notes ->
                    _uiState.update {
                        it.copy(
                            noteList = notes,
                            loadingState = false,
                            errorMessage = null
                        )
                    }
                }
            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        loadingState = false,
                        errorMessage = e.message ?: "Failed to load notes"
                    )
                }
            }
        }
    }

    fun loadNote(id: Long) {
        viewModelScope.launch {
            _uiState.update { it.copy(loadingState = true) }
            val note = repository.getNote(id)
            _uiState.update {
                it.copy(
                    selectedNote = note,
                    loadingState = false,
                    errorMessage = if (note == null) "Note not found" else null
                )
            }
        }
    }

    fun selectNote(note: Note) {
        _uiState.update {
            it.copy(selectedNote = note)
        }
    }

    fun addNote(
        title: String,
        content: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                val newNote = Note(
                    title = title.trim(),
                    content = content.trim(),
                    createdAt = LocalDateTime.now(),
                    updatedAt = LocalDateTime.now()
                )
                repository.createNote(newNote)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to add note") }
            }
        }
    }

    fun editNote(
        note: Note,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                repository.updateNote(note)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to update note") }
            }
        }
    }

    fun deleteNote(
        note: Note,
        onSuccess: () -> Unit = {}
    ) {
        viewModelScope.launch {
            try {
                repository.deleteNote(note)
                onSuccess()
            } catch (e: Exception) {
                _uiState.update { it.copy(errorMessage = e.message ?: "Failed to delete note") }
            }
        }
    }

    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }
}
