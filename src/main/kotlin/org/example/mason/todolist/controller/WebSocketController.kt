package org.example.mason.todolist.controller

import lombok.extern.slf4j.Slf4j
import org.example.mason.todolist.model.dto.PrivateChatMessage
import org.example.mason.todolist.model.dto.WebSocketMessage
import org.example.mason.todolist.model.entity.User
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.handler.annotation.MessageMapping
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.messaging.handler.annotation.SendTo
import org.springframework.messaging.simp.SimpMessageHeaderAccessor
import org.springframework.messaging.simp.SimpMessagingTemplate
import org.springframework.security.core.Authentication
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
        println("近來訊息")
        // 1. 將 Principal 轉型，取得我們需要的 Authentication 物件
        val auth = principal as Authentication
        // 2. 從 Authentication 物件中取得 UserEntity
        val user = auth.principal as User
        // 3. 從 UserEntity 取得真正的使用者名稱
        val actualUsername = user.userName

        // 4. 使用乾淨的使用者名稱來建立要廣播的訊息
        //    同時保留客戶端傳來的 content，並賦予新的 ID
        val copy = message.copy(sender = actualUsername, id = UUID.randomUUID().toString())

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
        messagingTemplate.convertAndSendToUser(copy.to, "/queue/reply", copy)

        // 發送確認給發送者
        messagingTemplate.convertAndSendToUser(copy.from, "/queue/reply", copy)
    }
}
