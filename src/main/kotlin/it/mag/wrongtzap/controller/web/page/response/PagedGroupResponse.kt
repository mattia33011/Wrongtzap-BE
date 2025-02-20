package it.mag.wrongtzap.controller.web.page.response

import it.mag.wrongtzap.controller.web.chat.response.GroupAltDTO

data class PagedGroupResponse (
    val pageNumber: Number,
    val pageSize: Number,
    val totalRecords: Long,
    val totalPages: Number,
    val content: List<GroupAltDTO>
)