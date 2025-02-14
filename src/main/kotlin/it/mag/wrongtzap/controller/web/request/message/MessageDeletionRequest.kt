package it.mag.wrongtzap.controller.web.request.message

class MessageDeletionRequest (
    val userId: Long,
    val chatId: Long,
    val messageId: Long,
    val type: String
)