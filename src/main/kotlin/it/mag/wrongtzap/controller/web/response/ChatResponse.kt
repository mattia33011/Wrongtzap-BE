package it.mag.wrongtzap.controller.web.response


data class ChatResponse(
    val chatId: String,
    val name: String,
    val messages: MutableList<MessageResponse>,
    val users: MutableSet<UserProfile>,
    val joinDate: MutableSet<JoinDateResponse>,
    val isGroup: Boolean
)