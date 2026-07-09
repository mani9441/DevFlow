package com.devflow.app.features.tasks.navigation

object TasksDestination {

    const val LIST = "tasks_list"

    const val ADD = "tasks_add"

    const val DETAILS = "tasks_details/{taskId}"

    fun details(taskId: Long): String {
        return "tasks_details/$taskId"
    }
}
