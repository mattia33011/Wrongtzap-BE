package it.mag.wrongtzap.response

import java.time.LocalDateTime

data class MessageResponse(
    val messageSender: String,
    val messageBody: String,
    val timestamp: LocalDateTime,
)