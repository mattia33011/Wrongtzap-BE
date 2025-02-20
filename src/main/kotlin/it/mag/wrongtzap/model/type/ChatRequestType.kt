package it.mag.wrongtzap.model.type

import it.mag.wrongtzap.controller.web.chat.request.ChatRequest
import it.mag.wrongtzap.controller.web.chat.request.GroupRequest

sealed class ChatRequestType {
    data class Direct(val direct: ChatRequest) : ChatRequestType()
    data class Group(val group: GroupRequest) : ChatRequestType()
}