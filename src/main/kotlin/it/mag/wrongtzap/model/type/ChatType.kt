package it.mag.wrongtzap.model.type

import it.mag.wrongtzap.model.DirectChat
import it.mag.wrongtzap.model.GroupChat

sealed class ChatType {
    data class Direct(val direct: DirectChat) : ChatType()
    data class Group(val group: GroupChat) : ChatType()
}