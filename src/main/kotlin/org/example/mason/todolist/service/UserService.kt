package org.example.mason.todolist.service

import org.example.mason.todolist.model.dto.UserDto
import org.example.mason.todolist.model.dto.UserRegAndLoginDto
import org.example.mason.todolist.model.entity.User
import org.example.mason.todolist.model.enum.Role
import org.example.mason.todolist.repo.UserRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUserName(username)
            .orElseThrow { UsernameNotFoundException("User not found with username: $username") }

        val authorities = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))

        return org.springframework.security.core.userdetails.User(user.userName, user.password, authorities)
    }

    fun createUser(userDto: UserRegAndLoginDto): UserDto {
        val newUser = User(userName = userDto.userName, password = passwordEncoder.encode(userDto.password), role = Role.USER)
        val savedUser = userRepository.save(newUser)
        return UserDto(id = savedUser.id!!, userName = savedUser.userName, role = savedUser.role)
    }

    fun getUserDtoByUsername(username: String): UserDto {
        val user = userRepository.findByUserName(username)
            .orElseThrow { UsernameNotFoundException("User not found with username: $username") }
        return UserDto(id = user.id!!, userName = user.userName, role = user.role)
    }

    /**
     * Finds a user by their username.
     * Used for JWT authentication where the user is already identified.
     */
    fun findByUsername(username: String): User? {
        return userRepository.findByUserName(username).orElse(null)
    }

    /**
     * Authenticates a user with a username and password.
     * Used for initial login.
     */
    fun login(username: String, password: String): User? {
        val user = findByUsername(username)
        // Check if user exists and if the provided password matches the stored hashed password
        if (user != null && passwordEncoder.matches(password, user.password)) {
            return user
        }
        return null
    }
}