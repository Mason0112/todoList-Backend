package org.example.mason.todolist.controller

import org.example.mason.todolist.model.dto.MessageType
import org.example.mason.todolist.model.dto.WebSocketMessage
import org.example.mason.todolist.model.dto.WebSocketMessageJoin
import org.example.mason.todolist.model.entity.User
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionConnectedEvent
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.*

@Component
class WebSocketEventListener(
    private val messagingTemplate: SimpMessageSendingOperations
) {

    private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    @EventListener
    fun handleWebSocketConnectListener(event: SessionConnectedEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val principal = headerAccessor.user

        if (principal != null) {
            // 1. 將 Principal 轉型回 Authentication 物件
            val authentication = principal as Authentication
            // 2. 從 Authentication 物件中取得我們當初設定的 UserEntity
            val user = authentication.principal as User
            // 3. 從 UserEntity 中取得乾淨的使用者名稱
            val username = user.userName // 假設你的 UserEntity 中儲存使用者名稱的欄位是 'userName'

            logger.info("User Connected: $username")

            // 建立一則乾淨的廣播訊息
            val chatMessage = WebSocketMessage(
                id = UUID.randomUUID().toString(),
                type = MessageType.JOIN,
                sender = "System", // JOIN 訊息的發送者通常是系統
                content = "$username joined!"
            )
            // 手動發送廣播
            messagingTemplate.convertAndSend("/topic/public", chatMessage)
        }
    }

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val principal = headerAccessor.user

        if (principal != null) {
            // 1. 將 Principal 轉型回 Authentication 物件
            val authentication = principal as Authentication
            // 2. 從 Authentication 物件中取得我們當初設定的 UserEntity
            val user = authentication.principal as User
            // 3. 從 UserEntity 中取得乾淨的使用者名稱
            val username = user.userName // 假設你的 UserEntity 中儲存使用者名稱的欄位是 'userName'

            logger.info("User Connected: $username")

            // 建立一則乾淨的廣播訊息
            val chatMessage = WebSocketMessage(
                id = UUID.randomUUID().toString(),
                type = MessageType.JOIN,
                sender = "System", // JOIN 訊息的發送者通常是系統
                content = "$username leaved!"
            )
            // 手動發送廣播
            messagingTemplate.convertAndSend("/topic/public", chatMessage)
        }
    }
}
