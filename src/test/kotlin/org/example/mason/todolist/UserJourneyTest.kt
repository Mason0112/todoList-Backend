package org.example.mason.todolist

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.example.mason.todolist.model.dto.CreateTodoListDto
import org.example.mason.todolist.model.dto.LoginResponseDto
import org.example.mason.todolist.model.dto.UserRegAndLoginDto
import org.example.mason.todolist.repo.TodoListRepository
import org.example.mason.todolist.repo.UserRepository
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.post

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class UserJourneyTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var userRepository: UserRepository
    @Autowired
    private lateinit var todoListRepository: TodoListRepository

    private val objectMapper = jacksonObjectMapper()

    @Test
    fun `test register, login, and create todo`() {
        todoListRepository.deleteAll()
        userRepository.deleteAll();

        val userCredentials = UserRegAndLoginDto("journeyUser", "password123")

        // 1. Register a new user
        mockMvc.post("/api/auth/register") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userCredentials)
        }.andExpect {
            status { isCreated() }
        }

        // 2. Login with the new user to get a JWT
        val loginResult = mockMvc.post("/api/auth/login") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(userCredentials)
        }.andExpect {
            status { isOk() }
        }.andReturn()

        val loginResponse: LoginResponseDto = objectMapper.readValue(loginResult.response.contentAsString)
        val jwt = loginResponse.token


        // 3. Create a new todo item
        val newTodo = CreateTodoListDto("Buy groceries")
        mockMvc.post("/api/todos"){
            header("Authorization", "Bearer $jwt")
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(newTodo)
        }.andExpect {
            status { isCreated() }
        }

        mockMvc.get("/api/todos")
        {
            header("Authorization", "Bearer $jwt")
        }.andExpect {
            status { isOk() }
            content { contentType(MediaType.APPLICATION_JSON) }
            jsonPath("$.length()") { value(1) }
        }
    }
}