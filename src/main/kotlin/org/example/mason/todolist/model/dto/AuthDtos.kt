package org.example.mason.todolist.model.dto

data class UserRegistrationDto(
    val userName: String,
    val password: String
)

data class LoginRequestDto(val userName: String, val password: String)

data class LoginResponseDto(val token: String)

data class UserDto(
    val id: Long,
    val userName: String)