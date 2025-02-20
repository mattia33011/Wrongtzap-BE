package it.mag.wrongtzap.controller.web.chat.response

import it.mag.wrongtzap.controller.web.page.response.PagedMessageResponse
import it.mag.wrongtzap.controller.web.user.response.ProfileResponse

data class ChatAltDTO (
    val chatId: String,
    val messages: PagedMessageResponse,
    val creationDate: Long,
    val members: List<ProfileResponse>,
    val archived: List<String>
)