package it.mag.wrongtzap.controller.web.request.user

data class LoginRequest(
    val userPassword: String,
    val userMail: String
)