package it.mag.wrongtzap.controller.web.chat.response

import it.mag.wrongtzap.controller.web.message.MessageResponse
import it.mag.wrongtzap.controller.web.user.response.ProfileResponse

class ChatDTO (
    val chatId: String,
    val messages: MutableList<MessageResponse>,
    val creationDate: Long,
    val members: List<ProfileResponse>,
    val archived: List<String>
)