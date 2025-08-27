package org.example.mason.todolist.model.dto

data class TodoListDto(
    val id: Long,
    val task: String,
    val completed: Boolean,
    val userId: Long // userId 應該是非空的
)

data class CreateTodoListDto(
    val task: String,
    val completed: Boolean = false
)