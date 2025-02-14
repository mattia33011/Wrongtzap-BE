package it.mag.wrongtzap.controller.web.request.user

data class FriendRequest (
    val senderId: Long,
    val receiverId: Long
)