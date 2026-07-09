package com.devflow.app.features.todo.data.mapper

import com.devflow.app.features.todo.data.local.entity.TodoEntity
import com.devflow.app.features.todo.domain.model.Todo
import com.devflow.app.features.todo.domain.model.TodoStatus

fun TodoEntity.toDomain(): Todo {

    return Todo(

        id = id,

        title = title,

        description = description,

        dueDate = dueDate,

        status = TodoStatus.valueOf(status),

        createdAt = createdAt,

        updatedAt = updatedAt
    )
}

fun Todo.toEntity(): TodoEntity {

    return TodoEntity(

        id = id,

        title = title,

        description = description,

        dueDate = dueDate,

        status = status.name,

        createdAt = createdAt,

        updatedAt = updatedAt
    )
}