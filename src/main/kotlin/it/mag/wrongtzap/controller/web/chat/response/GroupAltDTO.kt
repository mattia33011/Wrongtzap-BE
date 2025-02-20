package it.mag.wrongtzap.controller.web.chat.response

import it.mag.wrongtzap.controller.web.page.response.PagedMessageResponse
import it.mag.wrongtzap.controller.web.user.response.ProfileResponse

data class GroupAltDTO (
    val chatId: String,
    val name: String,
    val creationDate: Long,
    val messages: PagedMessageResponse,
    val users: MutableSet<ProfileResponse>,
    val joinDate: MutableSet<EntryDateDTO>,
    val archived: MutableList<String>
)