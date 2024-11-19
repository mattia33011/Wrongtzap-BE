package it.mag.wrongtzap.controller.web.request

class MessageRequest(
    val userId: String,
    val chatId: String,
    val body: String,
    val timestamp: Long,
)