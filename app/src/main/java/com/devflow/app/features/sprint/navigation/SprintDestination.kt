package com.devflow.app.features.sprint.navigation

object SprintDestination {

    const val LIST = "sprints_list"

    const val ADD = "sprints_add"

    const val DETAILS = "sprints_details/{sprintId}"

    fun details(sprintId: Long): String {
        return "sprints_details/$sprintId"
    }
}
