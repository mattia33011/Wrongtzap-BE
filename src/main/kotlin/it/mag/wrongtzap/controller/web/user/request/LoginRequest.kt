package it.mag.wrongtzap.controller.web.user.request

data class LoginRequest(
    val userPassword: String,
    val userMail: String
)