package com.devflow.app.features.todo.presentation.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import com.devflow.app.features.todo.presentation.viewmodel.TodoViewModel

@Composable
fun AddEditTodoScreen(
    todoId: Long?,
    onNavigateBack: () -> Unit,
    viewModel: TodoViewModel = hiltViewModel()
) {
    val state = viewModel.uiState.collectAsState().value

    var title by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var titleError by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(todoId) {
        if (todoId != null) {
            viewModel.loadTodo(todoId)
        }
    }

    LaunchedEffect(state.selectedTodo) {
        state.selectedTodo?.let { todo ->
            if (todoId == todo.id) {
                title = todo.title
                description = todo.description ?: ""
            }
        }
    }

    Scaffold(
        topBar = {
            AppTopBar(
                title = if (todoId == null) "Add Todo" else "Edit Todo",
                onBackClick = onNavigateBack
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
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
                value = description,
                onValueChange = { description = it },
                label = "Description"
            )

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Save",
                onClick = {
                    if (title.isBlank()) {
                        titleError = "Title cannot be empty"
                    } else {
                        if (todoId == null) {
                            viewModel.addTodo(
                                title = title,
                                description = description,
                                dueDate = null,
                                onSuccess = onNavigateBack
                            )
                        } else {
                            state.selectedTodo?.let { originalTodo ->
                                val updatedTodo = originalTodo.copy(
                                    title = title.trim(),
                                    description = description.trim()
                                )
                                viewModel.updateTodo(
                                    todo = updatedTodo,
                                    onSuccess = onNavigateBack
                                )
                            }
                        }
                    }
                }
            )
        }
    }
}
