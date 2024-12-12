package it.mag.wrongtzap.controller.web.response.chat

import it.mag.wrongtzap.controller.web.response.message.MessageResponse
import it.mag.wrongtzap.controller.web.response.user.UserProfileResponse

class DirectChatResponse (
    val chatId: String,
    val messages: MutableList<MessageResponse>,
    val creationDate: Long,
    val participants: List<UserProfileResponse>,
    val archived: List<String>
)