package org.example.mason.todolist.mapper

import org.example.mason.todolist.model.dto.CreateTodoListDto
import org.example.mason.todolist.model.dto.TodoListDto
import org.example.mason.todolist.model.entity.TodoList

//擴充函數
fun TodoList.toDto(): TodoListDto {
    return TodoListDto(
        id = this.id ?: throw IllegalArgumentException("ID cannot be null for DTO conversion"),
        task = this.task,
        completed = this.completed
    )
}

fun TodoListDto.toEntity(): TodoList {
    return TodoList(
        id = this.id,
        task = this.task,
        completed = this.completed
    )
}

fun CreateTodoListDto.toEntity(): TodoList {
    return TodoList(task = this.task, completed = this.completed)
}
