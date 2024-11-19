package it.mag.wrongtzap.controller.web.response

import java.time.LocalDateTime

data class JoinDateResponse (
    val userId: String,
    val timestamp: Long,
)