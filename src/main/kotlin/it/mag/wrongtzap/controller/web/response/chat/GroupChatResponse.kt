package it.mag.wrongtzap.controller.web.response.chat

import it.mag.wrongtzap.controller.web.response.message.MessageResponse
import it.mag.wrongtzap.controller.web.response.user.UserProfileResponse


data class GroupChatResponse(
    val chatId: String,
    val name: String,
    val creationDate: Long,
    val messages: MutableList<MessageResponse>,
    val users: MutableSet<UserProfileResponse>,
    val joinDate: MutableSet<JoinDateResponse>,
    val archived: MutableList<String>
)