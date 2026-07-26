package com.devflow.app.features.project.navigation

object ProjectDestination {

    const val LIST = "project_list"

    const val ADD = "project_add"

    const val EDIT = "project_edit/{projectId}"

    fun edit(projectId: Long): String {
        return "project_edit/$projectId"
    }
}
