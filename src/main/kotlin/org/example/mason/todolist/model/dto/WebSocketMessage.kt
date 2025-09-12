package org.example.mason.todolist.model.dto

enum class MessageType {
    CHAT, JOIN, LEAVE
}

data class WebSocketMessage(
    val id: String? = null,
    val type: MessageType,
    val sender: String?,
    val content: String?
)
data class WebSocketMessageJoin(
    val type: MessageType,
    val sender: String?,
)


// 私人訊息的資料結構
data class PrivateChatMessage(
    val id: String? = null,
    val from: String,
    val to: String,
    val content: String
)