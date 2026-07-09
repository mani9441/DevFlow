package com.devflow.app.features.deadlines.presentation.state

import com.devflow.app.features.deadlines.domain.model.Deadline

data class DeadlineUiState(
    val deadlineList: List<Deadline> = emptyList(),
    val selectedDeadline: Deadline? = null,
    val loadingState: Boolean = false,
    val errorMessage: String? = null
)
