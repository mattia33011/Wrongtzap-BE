package it.mag.wrongtzap.controller.web.user.response

data class FriendResponse (
    val friendshipId: String,
    val userId: String,
    val username: String,
    val status: String
)