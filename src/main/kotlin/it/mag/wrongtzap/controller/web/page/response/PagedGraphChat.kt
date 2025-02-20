package it.mag.wrongtzap.controller.web.page.response

import it.mag.wrongtzap.controller.web.chat.response.ChatDTO

data class PagedGraphChat (
    val pageNumber: Number,
    val pageSize: Number,
    val totalRecords: Long,
    val totalPages: Number,
    val content: List<ChatDTO>
)