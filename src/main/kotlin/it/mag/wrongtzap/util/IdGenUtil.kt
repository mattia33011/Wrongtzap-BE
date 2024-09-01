package it.mag.wrongtzap.util

import java.security.MessageDigest
import java.time.Instant

object IdGenUtil {

    fun generateUserId(name: String): String {
        val timestamp = Instant.now().toEpochMilli()
        val input = "$name$timestamp"
        val alphanumeric = input.toAlphanumericHash()
        return "$name-$alphanumeric"
    }

    fun generateChatId(chatName: String): String {
        val firstWord = chatName.split(" ").firstOrNull() ?: ""
        val timestamp = Instant.now().toEpochMilli()
        val input = "$firstWord$timestamp"
        val alphanumeric = input.toAlphanumericHash()
        return "$firstWord-$alphanumeric"
    }

    private fun String.toAlphanumericHash(): String {
        val bytes = this.toByteArray()
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(bytes)
        return digest.toHex().take(8).uppercase()
    }

    private fun ByteArray.toHex(): String {
        return joinToString(separator = "") { byte -> "%02x".format(byte) }
    }
}