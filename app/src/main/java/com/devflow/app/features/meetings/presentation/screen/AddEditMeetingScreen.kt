package com.devflow.app.features.meetings.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.app.core.designsystem.components.AppButton
import com.devflow.app.core.designsystem.components.AppTextField
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.features.meetings.domain.model.Meeting
import com.devflow.app.features.meetings.presentation.viewmodel.MeetingViewModel
import java.time.LocalDate
import java.time.format.DateTimeParseException

@Composable
fun AddEditMeetingScreen(
    meetingId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: MeetingViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    var title by remember { mutableStateOf("Daily Stand-up") }
    var meetingDateStr by remember { mutableStateOf(LocalDate.now().toString()) }
    var meetingTime by remember { mutableStateOf("09:30 AM") }
    var yesterdayWork by remember { mutableStateOf("") }
    var todayPlan by remember { mutableStateOf("") }
    var blockers by remember { mutableStateOf("") }

    var titleError by remember { mutableStateOf<String?>(null) }
    var dateError by remember { mutableStateOf<String?>(null) }
    var yesterdayError by remember { mutableStateOf<String?>(null) }
    var todayError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(meetingId) {
        if (meetingId != null) {
            viewModel.loadMeeting(meetingId)
        }
    }

    LaunchedEffect(state.selectedMeeting) {
        state.selectedMeeting?.let { meeting ->
            if (meetingId == meeting.id) {
                title = meeting.title
                meetingDateStr = meeting.meetingDate.toString()
                meetingTime = meeting.meetingTime
                yesterdayWork = meeting.yesterdayWork
                todayPlan = meeting.todayPlan
                blockers = meeting.blockers
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (meetingId == null) "Log Stand-up" else "Edit Stand-up",
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            AppTextField(
                value = title,
                onValueChange = {
                    title = it
                    if (titleError != null && it.isNotBlank()) {
                        titleError = null
                    }
                },
                label = "Title"
            )
            titleError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = meetingDateStr,
                onValueChange = {
                    meetingDateStr = it
                    if (dateError != null && it.isNotBlank()) {
                        dateError = null
                    }
                },
                label = "Meeting Date (YYYY-MM-DD)"
            )
            dateError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = meetingTime,
                onValueChange = { meetingTime = it },
                label = "Meeting Time (e.g. 09:30 AM)"
            )

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = yesterdayWork,
                onValueChange = {
                    yesterdayWork = it
                    if (yesterdayError != null && it.isNotBlank()) {
                        yesterdayError = null
                    }
                },
                label = "What did you do yesterday?",
                singleLine = false,
                minLines = 3
            )
            yesterdayError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = todayPlan,
                onValueChange = {
                    todayPlan = it
                    if (todayError != null && it.isNotBlank()) {
                        todayError = null
                    }
                },
                label = "What will you do today?",
                singleLine = false,
                minLines = 3
            )
            todayError?.let { error ->
                Text(
                    text = error,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 8.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            AppTextField(
                value = blockers,
                onValueChange = { blockers = it },
                label = "Any blockers? (Optional)",
                singleLine = false,
                minLines = 2
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                AppButton(
                    text = "Cancel",
                    onClick = onNavigateBack,
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(16.dp))
                AppButton(
                    text = "Save",
                    onClick = {
                        var hasError = false
                        if (title.isBlank()) {
                            titleError = "Title cannot be empty"
                            hasError = true
                        }
                        var parsedDate: LocalDate? = null
                        try {
                            parsedDate = LocalDate.parse(meetingDateStr.trim())
                        } catch (e: DateTimeParseException) {
                            dateError = "Invalid date format (must be YYYY-MM-DD)"
                            hasError = true
                        }
                        if (yesterdayWork.isBlank()) {
                            yesterdayError = "Yesterday's work cannot be empty"
                            hasError = true
                        }
                        if (todayPlan.isBlank()) {
                            todayError = "Today's plan cannot be empty"
                            hasError = true
                        }

                        if (!hasError && parsedDate != null) {
                            if (meetingId == null) {
                                viewModel.addMeeting(
                                    title = title,
                                    meetingDate = parsedDate,
                                    meetingTime = meetingTime,
                                    yesterdayWork = yesterdayWork,
                                    todayPlan = todayPlan,
                                    blockers = blockers,
                                    onSuccess = onNavigateBack
                                )
                            } else {
                                state.selectedMeeting?.let { originalMeeting ->
                                    val updatedMeeting = originalMeeting.copy(
                                        title = title.trim(),
                                        meetingDate = parsedDate,
                                        meetingTime = meetingTime.trim(),
                                        yesterdayWork = yesterdayWork.trim(),
                                        todayPlan = todayPlan.trim(),
                                        blockers = blockers.trim()
                                    )
                                    viewModel.editMeeting(
                                        meeting = updatedMeeting,
                                        onSuccess = onNavigateBack
                                    )
                                }
                            }
                        }
                    },
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
