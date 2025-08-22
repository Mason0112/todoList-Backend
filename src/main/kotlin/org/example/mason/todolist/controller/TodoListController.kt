package org.example.mason.todolist.controller

import org.example.mason.todolist.model.dto.CreateTodoListDto
import org.example.mason.todolist.model.dto.TodoListDto
import org.example.mason.todolist.service.TodoListService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/todos")
class TodoListController(private val todoListService: TodoListService) {

    @GetMapping
    fun getAllTodos(): ResponseEntity<List<TodoListDto>> {
        return ResponseEntity.ok(todoListService.getAllTodos())
    }

    @GetMapping("/{id}")
    fun getTodoById(@PathVariable id: Long): ResponseEntity<TodoListDto> {
        return todoListService.getTodoById(id)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @PostMapping
    fun createTodo(@RequestBody createDto: CreateTodoListDto): ResponseEntity<TodoListDto> {
        val createdTodo = todoListService.createTodo(createDto)
        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdTodo.id)
            .toUri()
        return ResponseEntity.created(location).body(createdTodo)
    }

    @PutMapping("/{id}")
    fun updateTodo(@PathVariable id: Long, @RequestBody todoDto: TodoListDto): ResponseEntity<TodoListDto> {
        if (todoDto.id != id) {
            return ResponseEntity.badRequest().build()
        }
        return todoListService.updateTodo(id, todoDto)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    fun deleteTodo(@PathVariable id: Long): ResponseEntity<Void> {
        todoListService.deleteTodo(id)
        return ResponseEntity.noContent().build()
    }
}