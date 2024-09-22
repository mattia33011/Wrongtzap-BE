package it.mag.wrongtzap.response

import java.time.LocalDateTime

data class JoinDateResponse (
    val userId: String,
    val timestamp: LocalDateTime,
)