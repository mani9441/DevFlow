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
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.devflow.app.core.designsystem.components.AppButton
import com.devflow.app.core.designsystem.components.AppTopBar
import com.devflow.app.core.designsystem.components.EmptyState
import com.devflow.app.core.designsystem.components.LoadingView
import com.devflow.app.features.todo.domain.model.TodoStatus
import com.devflow.app.features.todo.presentation.viewmodel.TodoViewModel

@Composable
fun TodoDetailsScreen(
    todoId: Long,
    onBackClick: () -> Unit,
    onEditClick: (Long) -> Unit,
    viewModel: TodoViewModel = hiltViewModel()
) {
    LaunchedEffect(todoId) {
        viewModel.loadTodo(todoId)
    }

    val state = viewModel.uiState.collectAsState().value
    val todo = state.selectedTodo

    Scaffold(
        topBar = {
            AppTopBar(
                title = "Todo Details",
                onBackClick = onBackClick
            )
        }
    ) { padding ->
        when {
            state.isLoading -> {
                LoadingView()
            }
            todo == null -> {
                EmptyState(message = "Todo not found.")
            }
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(16.dp)
                ) {
                    Text(
                        text = todo.title,
                        style = MaterialTheme.typography.headlineMedium
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Status: ${todo.status.name}",
                        style = MaterialTheme.typography.titleMedium,
                        color = if (todo.status == TodoStatus.COMPLETED) 
                            MaterialTheme.colorScheme.primary 
                        else 
                            MaterialTheme.colorScheme.secondary
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = todo.description ?: "No description provided.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Spacer(modifier = Modifier.height(32.dp))

                    if (todo.status == TodoStatus.PENDING) {
                        AppButton(
                            text = "Mark Completed",
                            onClick = {
                                viewModel.markCompleted(todo.id, onSuccess = onBackClick)
                            }
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    AppButton(
                        text = "Edit",
                        onClick = { onEditClick(todo.id) }
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AppButton(
                        text = "Delete",
                        onClick = {
                            viewModel.deleteTodo(todo, onSuccess = onBackClick)
                        }
                    )
                }
            }
        }
    }
}
