package it.mag.wrongtzap.controller.web.response

import org.hibernate.mapping.Join


data class ChatResponse(
    val chatId: String,
    val name: String,
    val messages: MutableList<MessageResponse>,
    val users: MutableSet<UserResponse>,
    val joinDate: MutableSet<JoinDateResponse>,
    val isGroup: Boolean
)