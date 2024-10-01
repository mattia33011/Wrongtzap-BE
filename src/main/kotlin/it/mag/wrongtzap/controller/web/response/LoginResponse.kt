package it.mag.wrongtzap.controller.web.response

data class LoginResponse(
    val token: String,
    val userMail: String
)