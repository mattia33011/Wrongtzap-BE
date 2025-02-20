package it.mag.wrongtzap.controller.web.page.response

import it.mag.wrongtzap.controller.web.user.response.FriendResponse

data class PagedFriendResponse (
    val pageNumber: Number,
    val pageSize: Number,
    val totalRecords: Long,
    val totalPages: Long,
    val content: MutableSet<FriendResponse>
)