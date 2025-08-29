package org.example.mason.todolist.model.dto

data class TodoListDto(
    val id: Long,
    val task: String,
    val completed: Boolean
)

data class CreateTodoListDto(
    val task: String,
    val completed: Boolean = false
)