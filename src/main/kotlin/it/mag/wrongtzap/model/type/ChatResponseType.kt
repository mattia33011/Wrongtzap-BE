package it.mag.wrongtzap.model.type

import it.mag.wrongtzap.controller.web.chat.response.ChatDTO
import it.mag.wrongtzap.controller.web.chat.response.GroupChatDTO


sealed class ChatResponseType {
    data class Direct(val direct: ChatDTO) : ChatResponseType()
    data class Group(val group: GroupChatDTO) : ChatResponseType()
}