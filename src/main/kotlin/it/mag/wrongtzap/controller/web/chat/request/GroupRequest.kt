package it.mag.wrongtzap.controller.web.chat.request

data class GroupRequest (
    val name: String,
    val adminId: String,
    val userIds: List<String>,
)