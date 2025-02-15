package it.mag.wrongtzap.controller.web.request.message

class MessageDeletionRequest (
    val userId: String,
    val chatId: String,
    val messageId: String,
    val type: String
)