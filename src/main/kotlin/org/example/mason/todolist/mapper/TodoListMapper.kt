package org.example.mason.todolist.mapper

import org.example.mason.todolist.model.dto.TodoListDto
import org.example.mason.todolist.model.entity.TodoList
import org.example.mason.todolist.model.entity.User

/**
 * 將資料庫實體 `TodoList` 轉換為資料傳輸物件 `TodoListDto`。
 * 這是安全的，因為從資料庫取出的 TodoList 必定有 ID，其關聯的 User 也必定有 ID。
 */
fun TodoList.toDto(): TodoListDto {
    return TodoListDto(
        id = this.id!!,
        task = this.task,
        completed = this.completed,
        userId = this.user.id!!
    )
}

/**
 * 將用於建立的 DTO `CreateTodoListDto` 轉換為資料庫實體 `TodoList`。
 * @param user 這個 TodoList 將屬於哪個使用者。這個 User 物件由 Service 層提供。
 */
fun org.example.mason.todolist.model.dto.CreateTodoListDto.toEntity(user: User): TodoList {
    return TodoList(task = this.task, completed = this.completed, user = user)
}
