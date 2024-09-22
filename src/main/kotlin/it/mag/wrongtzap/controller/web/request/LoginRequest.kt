package it.mag.wrongtzap.controller.web.request

data class LoginRequest(
    val userPassword: String,
    val userMail: String
) {}