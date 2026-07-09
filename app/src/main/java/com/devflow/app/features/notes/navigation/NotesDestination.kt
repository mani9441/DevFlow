package com.devflow.app.features.notes.navigation

object NotesDestination {

    const val LIST = "notes_list"

    const val ADD = "notes_add"

    const val DETAILS = "notes_details/{noteId}"

    fun details(noteId: Long): String {
        return "notes_details/$noteId"
    }
}
