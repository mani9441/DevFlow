package com.devflow.app.features.stories.navigation

object StoriesDestination {

    const val LIST = "stories_list"

    const val ADD = "stories_add"

    const val DETAILS = "stories_details/{storyId}"

    fun details(storyId: Long): String {
        return "stories_details/$storyId"
    }
}
