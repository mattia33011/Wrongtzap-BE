package it.mag.wrongtzap.controller.web.request.chat

data class GroupChatRequest (
    val chatName: String,
    val firstUserId: String,
    val chatUsersIds: List<String>,
    val isGroup: Boolean
)