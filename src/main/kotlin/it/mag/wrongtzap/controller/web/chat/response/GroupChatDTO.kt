package it.mag.wrongtzap.controller.web.chat.response

import it.mag.wrongtzap.controller.web.message.MessageResponse
import it.mag.wrongtzap.controller.web.user.response.ProfileResponse


data class GroupChatDTO(
    val chatId: String,
    val name: String,
    val creationDate: Long,
    val messages: MutableList<MessageResponse>,
    val users: MutableSet<ProfileResponse>,
    val joinDate: MutableSet<EntryDateDTO>,
    val archived: MutableList<String>
)