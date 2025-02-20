package it.mag.wrongtzap.controller.web.page.request

data class PageChatRequest (
    val userId: String,
    val pageSize: Number,
    val pageNumber: Number,
)