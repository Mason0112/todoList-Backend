package org.example.mason.todolist.controller

import org.example.mason.todolist.model.dto.MessageType
import org.example.mason.todolist.model.dto.WebSocketMessage
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.messaging.simp.SimpMessageSendingOperations
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.stereotype.Component
import org.springframework.web.socket.messaging.SessionDisconnectEvent
import java.util.UUID

@Component
class WebSocketEventListener(
    private val messagingTemplate: SimpMessageSendingOperations
) {

    private val logger = LoggerFactory.getLogger(WebSocketEventListener::class.java)

    @EventListener
    fun handleWebSocketDisconnectListener(event: SessionDisconnectEvent) {
        val headerAccessor = StompHeaderAccessor.wrap(event.message)
        val username = headerAccessor.sessionAttributes?.get("username") as? String

        if (username != null) {
            logger.info("User Disconnected: $username")

            val chatMessage = WebSocketMessage(
                id = UUID.randomUUID().toString(),
                type = MessageType.LEAVE,
                sender = username,
                content = null
            )

            messagingTemplate.convertAndSend("/topic/public", chatMessage)
        }
    }
}
