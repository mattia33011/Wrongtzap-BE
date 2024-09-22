package it.mag.wrongtzap.request

data class LoginRequest(
    val userPassword: String,
    val userMail: String
) {}