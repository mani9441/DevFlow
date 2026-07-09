package com.devflow.app.features.todo.navigation

object TodoDestination {

    const val LIST = "todo_list"

    const val ADD = "todo_add"

    const val DETAILS = "todo_details/{todoId}"

    fun details(todoId: Long): String {
        return "todo_details/$todoId"
    }
}
