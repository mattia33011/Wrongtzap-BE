package it.mag.wrongtzap.controller.web.user.request

data class UpdateFriendRequest (
    val friendshipId: String,
    val senderId: String,
    val receiverId: String,
)