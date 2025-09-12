package org.example.mason.todolist.controller

import org.example.mason.todolist.model.dto.PrivateChatMessage
import org.example.mason.todolist.model.dto.WebSocketMessage
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.stereotype.Controller
import java.security.Principal
import java.util.UUID

@Controller
class WebSocketController {
    @Autowired
    private lateinit var messagingTemplate: SimpMessagingTemplate


    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    fun sendMessage(
        @Payload message: WebSocketMessage,
        principal: Principal
    ): WebSocketMessage {
        val copy = message.copy(sender = principal.name, id = UUID.randomUUID().toString())
        return copy
    }

    @MessageMapping("/chat.addUser")
    @SendTo("/topic/public")
    fun addUser(
        @Payload message: WebSocketMessage,
        headerAccessor: SimpMessageHeaderAccessor,
        principal: Principal
    ): WebSocketMessage {
        val sender = principal.name
        val copy = message.copy(sender = sender, id = UUID.randomUUID().toString())
        headerAccessor.sessionAttributes!!["username"] = sender
        return copy
    }

    /**
     * 處理私人訊息
     * 這就是私人訊息流程的核心
     */
    @MessageMapping("/chat.privateMessage")
    fun sendPrivateMessage(@Payload message: PrivateChatMessage, principal: Principal) {

        val actualSender = principal.name
        val copy = message.copy(from = actualSender, id = UUID.randomUUID().toString())
        // 發送私人訊息
        messagingTemplate.convertAndSendToUser(copy.to, "/queue/reply", message)

        // 發送確認給發送者
        messagingTemplate.convertAndSendToUser(copy.from, "/queue/reply", message)
    }
}
