package it.mag.wrongtzap.controller.web.response.chat

import it.mag.wrongtzap.controller.web.response.message.MessageResponse
import it.mag.wrongtzap.controller.web.response.user.ProfileResponse

class DirectChatResponse (
    val chatId: String,
    val messages: MutableList<MessageResponse>,
    val creationDate: Long,
    val participants: List<ProfileResponse>,
    val archived: List<String>
)