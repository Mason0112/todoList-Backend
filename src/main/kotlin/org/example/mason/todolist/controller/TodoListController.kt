package org.example.mason.todolist.controller

import org.example.mason.todolist.model.dto.CreateTodoListDto
import org.example.mason.todolist.model.dto.TodoListDto
import org.example.mason.todolist.service.TodoListService
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.net.URI

@RestController
@RequestMapping("/api/todos")
class TodoListController(private val todoListService: TodoListService) {

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun getMyTodos(@AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<List<TodoListDto>> {
        return ResponseEntity.ok(todoListService.getTodosForUser(userDetails.username))
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun createTodo(@RequestBody createDto: CreateTodoListDto, @AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<TodoListDto> {
        val createdTodo = todoListService.createTodo(createDto, userDetails.username)
        val location: URI = ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(createdTodo.id)
            .toUri()
        return ResponseEntity.created(location).body(createdTodo)
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun updateTodo(@PathVariable id: Long, @RequestBody todoDto: TodoListDto, @AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<TodoListDto> {
        if (todoDto.id != id) {
            return ResponseEntity.badRequest().build()
        }
        return todoListService.updateTodo(id, todoDto, userDetails.username)
            ?.let { ResponseEntity.ok(it) }
            ?: ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    fun deleteTodo(@PathVariable id: Long, @AuthenticationPrincipal userDetails: UserDetails): ResponseEntity<Void> {
        todoListService.deleteTodo(id, userDetails.username)
        return ResponseEntity.noContent().build()
    }
}