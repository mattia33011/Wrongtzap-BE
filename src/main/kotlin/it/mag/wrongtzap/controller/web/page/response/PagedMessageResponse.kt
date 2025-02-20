package it.mag.wrongtzap.controller.web.page.response

import it.mag.wrongtzap.controller.web.message.MessageResponse

data class PagedMessageResponse (
    val pageNumber: Number,
    val pageSize: Number,
    val totalRecords: Long,
    val totalPages: Number,
    val content: List<MessageResponse>
)