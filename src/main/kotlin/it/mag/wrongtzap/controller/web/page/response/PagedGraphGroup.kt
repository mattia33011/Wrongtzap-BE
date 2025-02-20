package it.mag.wrongtzap.controller.web.page.response

import it.mag.wrongtzap.controller.web.chat.response.GroupChatDTO

class PagedGraphGroup (
    val pageNumber: Number,
    val pageSize: Number,
    val totalRecords: Long,
    val totalPages: Number,
    val content: List<GroupChatDTO>
)