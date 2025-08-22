package org.example.mason.todolist.model.dto

data class CreateTodoListDto(
    val task: String,
    val completed: Boolean = false
)