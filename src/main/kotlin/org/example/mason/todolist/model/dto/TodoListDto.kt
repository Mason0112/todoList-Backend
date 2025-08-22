package org.example.mason.todolist.model.dto

data class TodoListDto (
    var id: Long,
    val task: String,
    val completed: Boolean
)