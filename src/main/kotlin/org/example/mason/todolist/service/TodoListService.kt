package org.example.mason.todolist.service

import org.example.mason.todolist.mapper.toDto
import org.example.mason.todolist.mapper.toEntity
import org.example.mason.todolist.model.dto.CreateTodoListDto
import org.example.mason.todolist.model.dto.TodoListDto
import org.example.mason.todolist.repo.TodoListRepository
import org.springframework.stereotype.Service
import kotlin.jvm.optionals.getOrNull

@Service
class TodoListService(private val todoListRepository: TodoListRepository) {

    fun getAllTodos(): List<TodoListDto> {
        return todoListRepository.findAll().map { it.toDto() }
    }

    fun getTodoById(id: Long): TodoListDto? {
        // .getOrNull() 是更符合 Kotlin 風格的 Optional 處理方式
        return todoListRepository.findById(id).map { it.toDto() }.getOrNull()
    }

    fun createTodo(createDto: CreateTodoListDto): TodoListDto {
        val entity = createDto.toEntity()
        val savedEntity = todoListRepository.save(entity)
        return savedEntity.toDto()
    }

    fun updateTodo(id: Long, todoDto: TodoListDto): TodoListDto? {
        return todoListRepository.findById(id).map { existingTodo ->
            existingTodo.task = todoDto.task
            existingTodo.completed = todoDto.completed
            todoListRepository.save(existingTodo).toDto()
        }.getOrNull()
    }
    fun deleteTodo(id: Long) {
        todoListRepository.deleteById(id)
    }
}