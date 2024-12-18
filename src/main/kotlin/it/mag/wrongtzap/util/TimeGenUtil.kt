package it.mag.wrongtzap.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeGenUtil {
    fun millisecondsFormat(): String {
        val format = DateTimeFormatter.ofPattern("HH-mm-ss.SSS")
        return LocalDateTime.now().format(format)
    }
}