package org.example.mason.todolist.repo

import org.example.mason.todolist.model.entity.TodoList
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface TodoListRepository  : JpaRepository<TodoList, Long>{
}