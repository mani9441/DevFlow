package com.devflow.app.features.issues.navigation

object IssuesDestination {

    const val LIST = "issues_list"

    const val ADD = "issues_add"

    const val DETAILS = "issues_details/{issueId}"

    fun details(issueId: Long): String {
        return "issues_details/$issueId"
    }
}
