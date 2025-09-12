package org.example.mason.todolist.config

import org.example.mason.todolist.security.CustomTokenAuthenticationManager
import org.example.mason.todolist.security.JwtAuthentication
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.messaging.support.MessageHeaderAccessor
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component

@Component
class AuthChannelInterceptor(
    private val customTokenAuthenticationManager: CustomTokenAuthenticationManager // 注入你寫好的 Manager
) : ChannelInterceptor {

    override fun preSend(message: Message<*>, channel: MessageChannel): Message<*>? {
        val accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor::class.java)

        // 只在 CONNECT 命令時進行認證
        if (accessor != null && StompCommand.CONNECT == accessor.command) {
            // 從 header 中獲取 token
            val authorizationHeader = accessor.getFirstNativeHeader("Authorization")
            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
                val token = authorizationHeader.substring(7)

                // 1. 建立一個未認證的 JwtAuthentication 物件
                val authenticationRequest = JwtAuthentication(token)

                // 2. 使用你的 AuthenticationManager 進行認證
                val authenticationResult = customTokenAuthenticationManager.authenticate(authenticationRequest)

                // 3. 如果認證成功，將其設定到 Security Context 和 WebSocket Session 中
                if (authenticationResult != null && authenticationResult.isAuthenticated) {
                    SecurityContextHolder.getContext().authentication = authenticationResult
                    accessor.user = authenticationResult // 這一步很重要，讓 @MessageMapping 方法能拿到 Principal
                }
            }
        }
        return message
    }
}