package org.example.mason.todolist.service

import org.example.mason.todolist.model.dto.CreateTodoListDto
import org.example.mason.todolist.model.dto.TodoListDto
import org.example.mason.todolist.mapper.toDto
import org.example.mason.todolist.mapper.toEntity
import org.example.mason.todolist.repo.TodoListRepository
import org.example.mason.todolist.repo.UserRepository
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class TodoListService(
    private val todoListRepository: TodoListRepository,
    private val userRepository: UserRepository
) {

    fun getTodosForUser(username: String): List<TodoListDto> {
        val user = userRepository.findByUserName(username)
            .orElseThrow { UsernameNotFoundException("User not found: $username") }
        return todoListRepository.findByUser(user).map { it.toDto() }
    }

    fun createTodo(createDto: CreateTodoListDto, username: String): TodoListDto {
        val user = userRepository.findByUserName(username)
            .orElseThrow { UsernameNotFoundException("User not found: $username") }
        // 使用 mapper 將 DTO 和 User 組合為一個 Entity
        val newTodo = createDto.toEntity(user)
        return todoListRepository.save(newTodo).toDto()
    }

    fun updateTodo(id: Long, todoDto: TodoListDto, username: String): TodoListDto? {
        val todo = verifyAndGetTodo(id, username)
        todo.task = todoDto.task
        todo.completed = todoDto.completed
        return todoListRepository.save(todo).toDto()
    }

    fun deleteTodo(id: Long, username: String) {
        val todo = verifyAndGetTodo(id, username)
        todoListRepository.delete(todo)
    }

    private fun verifyAndGetTodo(id: Long, username: String): org.example.mason.todolist.model.entity.TodoList {
        val todo = todoListRepository.findById(id)
            .orElseThrow { NoSuchElementException("Todo with id $id not found") }
        if (todo.user.userName != username) {
            throw AccessDeniedException("You do not have permission to access this todo")
        }
        return todo
    }
}