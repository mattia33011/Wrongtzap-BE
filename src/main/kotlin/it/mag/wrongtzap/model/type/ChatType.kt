package it.mag.wrongtzap.model.type

import it.mag.wrongtzap.model.Chat

sealed class ChatType {
    data class Direct(val direct: Chat) : ChatType()
    data class Group(val group: it.mag.wrongtzap.model.GroupChat) : ChatType()
}