package it.mag.wrongtzap.controller.web.message

data class MessageResponse(
    val username: String,
    val userId: String,
    val chatId: String,
    val content: String,
    val timestamp: Number
)