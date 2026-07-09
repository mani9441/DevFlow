package com.devflow.app.navigation

sealed class NavRoutes(val route: String) {

    data object Dashboard : NavRoutes("dashboard")

    data object Todo : NavRoutes("todo_list")

    data object Tasks : NavRoutes("tasks")

    data object Sprint : NavRoutes("sprint")

    data object Stories : NavRoutes("stories")

    data object Progress : NavRoutes("progress")

    data object Meetings : NavRoutes("meetings")

    data object Deadlines : NavRoutes("deadlines")

    data object Notes : NavRoutes("notes")

    data object Issues : NavRoutes("issues")

    data object Communication : NavRoutes("communication")

    data object CI : NavRoutes("ci")
}