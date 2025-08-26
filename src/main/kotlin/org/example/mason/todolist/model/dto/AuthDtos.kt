package org.example.mason.todolist.model.dto

import org.example.mason.todolist.model.enum.Role
data class UserRegistrationDto(
    val userName: String,
    val password: String
)
data class LoginRequestDto(val userName: String, val password: String)

data class LoginResponseDto(
    val token: String,
    val user: UserDto
)

data class UserDto(
    val id: Long,
    val userName: String,
    val role: Role
)