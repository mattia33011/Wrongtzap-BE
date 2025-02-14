package it.mag.wrongtzap.controller.web.request.message

class MessageRequest(
    val userId: Long,
    val chatId: Long,
    val body: String,
    val type: String
)