package it.mag.wrongtzap.controller.web.request

data class UserRequest(
    val userName: String,
    val userMail: String,
    val userPassword: String
)