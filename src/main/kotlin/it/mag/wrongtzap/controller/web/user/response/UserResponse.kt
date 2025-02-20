package it.mag.wrongtzap.controller.web.user.response

import it.mag.wrongtzap.controller.web.chat.response.ChatDTO
import it.mag.wrongtzap.controller.web.chat.response.GroupChatDTO

data class UserResponse(
    val userId: String,
    val username: String,
    val directChats: MutableSet<ChatDTO>,
    val groupChats: MutableSet<GroupChatDTO>,
    val friends: MutableSet<FriendResponse>
) {}