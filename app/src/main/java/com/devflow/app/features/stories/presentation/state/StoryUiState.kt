package com.devflow.app.features.stories.presentation.state

import com.devflow.app.features.stories.domain.model.UserStory
import com.devflow.app.features.sprint.domain.model.Sprint

data class StoryUiState(
    val storyList: List<UserStory> = emptyList(),
    val unassignedStories: List<UserStory> = emptyList(),
    val selectedStory: UserStory? = null,
    val availableSprints: List<Sprint> = emptyList(),
    val loadingState: Boolean = false,
    val errorMessage: String? = null
)
