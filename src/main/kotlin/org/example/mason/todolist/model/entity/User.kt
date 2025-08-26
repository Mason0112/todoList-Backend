package org.example.mason.todolist.model.entity

import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import org.example.mason.todolist.model.enum.Role

@Entity
@Table(name = "users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 自動產生 ID
    var id: Long? = null,
    var userName: String,
    var password: String,
    @Enumerated(EnumType.STRING)
    var role: Role
)