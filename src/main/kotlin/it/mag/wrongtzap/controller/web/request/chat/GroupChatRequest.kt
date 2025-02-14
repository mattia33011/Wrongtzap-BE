package it.mag.wrongtzap.controller.web.request.chat

data class GroupChatRequest (
    val name: String,
    val adminId: Long,
    val userIds: List<Long>,
)