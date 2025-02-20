package it.mag.wrongtzap.controller.web.page.response.base

import it.mag.wrongtzap.model.GroupChat

data class PagedGroup (
    val pageNumber: Number,
    val pageSize: Number,
    val totalRecords: Long,
    val totalPages: Number,
    val content: List<GroupChat>
)