package it.mag.wrongtzap.controller.web.response.user

import it.mag.wrongtzap.controller.web.response.chat.DirectChatResponse
import it.mag.wrongtzap.controller.web.response.chat.GroupChatResponse

data class UserResponse(
    val userId: String,
    val username: String,
    val directChats: MutableSet<DirectChatResponse>,
    val groupChats: MutableSet<GroupChatResponse>,
    val friends: MutableSet<UserProfileResponse>
) {}