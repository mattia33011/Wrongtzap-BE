package it.mag.wrongtzap.controller.web.user.request

data class PendingFriendRequest (
    val senderId: String,
    val receiverId: String
)