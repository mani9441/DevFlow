package com.devflow.app.features.deadlines.navigation

object DeadlinesDestination {

    const val LIST = "deadlines_list"

    const val ADD = "deadlines_add"

    const val DETAILS = "deadlines_details/{deadlineId}"

    fun details(deadlineId: Long): String {
        return "deadlines_details/$deadlineId"
    }
}
