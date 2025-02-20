package it.mag.wrongtzap.controller.web.user.request

data class RegisterRequest(
    val userName: String,
    val userMail: String,
    val userPassword: String
)