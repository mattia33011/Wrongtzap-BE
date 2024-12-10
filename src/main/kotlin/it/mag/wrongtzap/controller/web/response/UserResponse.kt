package it.mag.wrongtzap.controller.web.response

data class UserResponse(
    val userId: String,
    val username: String,
    val chats: MutableSet<ChatResponse>,
    val friends: MutableSet<UserProfile>
) {}