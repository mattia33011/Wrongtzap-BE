package it.mag.wrongtzap.controller.web.response

import java.time.LocalDateTime

data class MessageResponse(
    val sender: String,
    val content: String,
    val timestamp: LocalDateTime,
)