package it.mag.wrongtzap.controller.web.page.request

data class PageMessageRequest (
    val userId: String,
    val chatId: String,
    val pageSize: Number,
    val pageNumber: Number
)