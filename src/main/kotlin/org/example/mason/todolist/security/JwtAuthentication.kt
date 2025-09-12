package org.example.mason.todolist.security

import org.example.mason.todolist.model.entity.User
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority

class JwtAuthentication(
    val token: String,
    // UserEntity is your application's model for your user
    val user: User? = null,
    authorities: Collection<GrantedAuthority>? = null) : AbstractAuthenticationToken(authorities) {

    override fun getCredentials(): Any? = token

    override fun getName(): String? = user?.userName

    override fun getPrincipal(): Any? = user
}