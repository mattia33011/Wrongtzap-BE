package it.mag.wrongtzap.controller.web.response

data class MessageResponse(
    val sender: String,
    val chatId: String,
    val content: String,
    val timestamp: Number
)