package it.mag.wrongtzap.controller.web.page.response.base

import it.mag.wrongtzap.model.Chat

data class PagedChat (
    val pageNumber: Number,
    val pageSize: Number,
    val totalRecords: Long,
    val totalPages: Number,
    val content: List<Chat>
)