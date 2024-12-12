package it.mag.wrongtzap.model.type

import it.mag.wrongtzap.controller.web.response.chat.DirectChatResponse
import it.mag.wrongtzap.controller.web.response.chat.GroupChatResponse


sealed class ChatResponseType {
    data class Direct(val direct: DirectChatResponse) : ChatResponseType()
    data class Group(val group: GroupChatResponse) : ChatResponseType()
}