package it.mag.wrongtzap.model.type

import it.mag.wrongtzap.controller.web.request.chat.DirectChatRequest
import it.mag.wrongtzap.controller.web.request.chat.GroupChatRequest

sealed class ChatRequestType {
    data class Direct(val direct: DirectChatRequest) : ChatRequestType()
    data class Group(val group: GroupChatRequest) : ChatRequestType()
}