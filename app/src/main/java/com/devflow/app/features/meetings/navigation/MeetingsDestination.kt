package com.devflow.app.features.meetings.navigation

object MeetingsDestination {

    const val LIST = "meetings_list"

    const val ADD = "meetings_add"

    const val DETAILS = "meetings_details/{meetingId}"

    fun details(meetingId: Long): String {
        return "meetings_details/$meetingId"
    }
}
