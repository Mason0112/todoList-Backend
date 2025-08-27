package org.example.mason.todolist.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "todo_list") // 加上表格名稱，以防與 SQL 關鍵字衝突
data class TodoList(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動產生 ID
    var id: Long? = null, // ID 設為可空，因為在建立新物件時，ID 尚未產生

    var task: String,

    var completed: Boolean = false, // 任務完成狀態，預設為 false (未完成)

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    var user: User
)