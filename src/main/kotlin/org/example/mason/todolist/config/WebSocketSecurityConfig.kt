package org.example.mason.todolist.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.messaging.Message
import org.springframework.messaging.simp.SimpMessageType
import org.springframework.security.authorization.AuthorizationManager
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager

@Configuration
@EnableWebSocketSecurity
class WebSocketSecurityConfig {

    @Bean
    fun messageAuthorizationManager(): AuthorizationManager<Message<*>> {
        return MessageMatcherDelegatingAuthorizationManager.builder()
            .nullDestMatcher().authenticated() // 連接時需要認證
            .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
            .simpDestMatchers("/app/**").authenticated()
            .simpSubscribeDestMatchers("/user/**", "/topic/friends/*").authenticated()
            .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE).authenticated()
            .anyMessage().authenticated()
            .build()
    }

}