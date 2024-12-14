package it.mag.wrongtzap.controller.web.request.message

class MessageRequest(
    val userId: String,
    val chatId: String,
    val body: String,
    val type: String
)