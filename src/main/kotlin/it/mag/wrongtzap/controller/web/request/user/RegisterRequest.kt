package it.mag.wrongtzap.controller.web.request.user

data class RegisterRequest(
    val userName: String,
    val userMail: String,
    val userPassword: String
)