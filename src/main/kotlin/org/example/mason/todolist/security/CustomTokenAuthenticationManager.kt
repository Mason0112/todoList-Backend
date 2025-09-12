package org.example.mason.todolist.security

import org.example.mason.todolist.model.entity.User
import org.example.mason.todolist.service.UserService
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component

/**
 * A custom AuthenticationManager that handles both JWT and Username/Password authentication.
 */
@Component("customTokenAuthenticationManager")
class CustomTokenAuthenticationManager(
    private val jwtTokenProvider: JwtTokenProvider,
    private val userService: UserService // Correctly inject UserService
) : AuthenticationManager {

    override fun authenticate(authentication: Authentication): Authentication {
        return when (authentication) {
            is JwtAuthentication -> {
                val user = findUser(authentication)
                checkUser(user)
                // Return a fully authenticated object with the user principal and authorities
                UsernamePasswordAuthenticationToken(user, null, authoritiesOf(user))
            }
            is UsernamePasswordAuthenticationToken -> {
                val user = findUser(authentication)
                checkUser(user)
                authentication.withGrantedAuthorities(user)
            }
            else -> authentication
        }
    }

    private fun findUser(authentication: JwtAuthentication): User {
        val jwtToken = authentication.token
            ?: throw BadCredentialsException("JWT Token is missing from the authentication request.")

        if (!jwtTokenProvider.validateToken(jwtToken)) {
            throw BadCredentialsException("Invalid JWT Token provided.")
        }

        val username = jwtTokenProvider.getAuthentication(jwtToken).name
        return userService.findByUsername(username)
            ?: throw BadCredentialsException("No user associated with token or token is invalid.")
    }

    private fun findUser(authentication: UsernamePasswordAuthenticationToken): User = 
        userService.login(authentication.principal.toString(), authentication.credentials.toString())
            ?: throw BadCredentialsException("Invalid login credentials.")

    @Suppress("unused", "UNUSED_PARAMETER")
    private fun checkUser(user: User) {
        // TODO: Add these checks and lock account on x attempts
        // if(!user.enabled) throw DisabledException("User is disabled.")
        // if(user.accountLocked) throw LockedException("User account is locked.")
    }

    private fun UsernamePasswordAuthenticationToken.withGrantedAuthorities(user: User): UsernamePasswordAuthenticationToken {
        return UsernamePasswordAuthenticationToken(user, credentials, authoritiesOf(user))
    }

    private fun authoritiesOf(user: User) = listOf(SimpleGrantedAuthority("ROLE_${user.role.name}"))
}
