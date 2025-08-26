package org.example.mason.todolist.mapper

import org.example.mason.todolist.model.dto.UserDto
import org.example.mason.todolist.model.entity.User

fun User.toDto(): UserDto {
    return UserDto(
        id = this.id ?: throw IllegalStateException("User ID cannot be null for DTO conversion"),
        userName = this.userName,
        role = this.role
    )
}