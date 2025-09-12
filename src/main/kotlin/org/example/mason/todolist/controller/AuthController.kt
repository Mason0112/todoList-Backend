package org.example.mason.todolist.controller

import org.example.mason.todolist.model.dto.LoginResponseDto
import org.example.mason.todolist.model.dto.UserRegAndLoginDto
import org.example.mason.todolist.security.JwtTokenProvider
import org.example.mason.todolist.service.UserService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/auth")
class AuthController(
    private val authenticationManager: AuthenticationManager,
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService
) {


    @GetMapping("/me")
    fun getCurrentUser(@AuthenticationPrincipal principal: UserDetails): ResponseEntity<LoginResponseDto> {
        val userDto = userService.getUserDtoByUsername(principal.username)
        val loginResponse = LoginResponseDto(token = null, user = userDto)
        return ResponseEntity.ok(loginResponse)
    }

    @PostMapping("/login")
    fun authenticateUser(@RequestBody loginRequest: UserRegAndLoginDto): ResponseEntity<LoginResponseDto> {
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(loginRequest.userName, loginRequest.password)
        )
        SecurityContextHolder.getContext().authentication = authentication
        return createLoginResponse(authentication)
    }

    @PostMapping("/register")
    fun registerUser(@RequestBody registerDto: UserRegAndLoginDto): ResponseEntity<LoginResponseDto> {
        // 1. 先建立使用者
        userService.createUser(registerDto)

        // 2. 建立成功後，立刻為他們進行認證
        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(registerDto.userName, registerDto.password)
        )
        SecurityContextHolder.getContext().authentication = authentication

        // 3. 產生 Token 並回傳 LoginResponseDto
        return createLoginResponse(authentication, HttpStatus.CREATED)
    }

    private fun createLoginResponse(authentication: Authentication, status: HttpStatus = HttpStatus.OK): ResponseEntity<LoginResponseDto> {
        val userDetails = authentication.principal as org.springframework.security.core.userdetails.UserDetails
        val jwt = jwtTokenProvider.generateToken(userDetails)
        val userDto = userService.getUserDtoByUsername(userDetails.username)
        val loginResponse = LoginResponseDto(token = jwt, user = userDto)
        return ResponseEntity(loginResponse, status)
    }


}