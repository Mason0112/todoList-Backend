package org.example.mason.todolist.repo

import org.example.mason.todolist.model.entity.TodoList
import org.example.mason.todolist.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository

interface TodoListRepository : JpaRepository<TodoList, Long> {
    fun findByUser(user: User): List<TodoList>
}