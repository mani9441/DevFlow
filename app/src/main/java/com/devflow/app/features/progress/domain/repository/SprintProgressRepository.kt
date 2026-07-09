package com.devflow.app.features.progress.domain.repository

import com.devflow.app.features.progress.domain.model.SprintProgress
import kotlinx.coroutines.flow.Flow

interface SprintProgressRepository {

    fun getProgressForSprint(sprintId: Long): Flow<SprintProgress>
}
