package org.example.mason.todolist.repo

import org.example.mason.todolist.model.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserRepository : JpaRepository<User, Long> {
    fun findByUserName(userName: String): Optional<User>
}