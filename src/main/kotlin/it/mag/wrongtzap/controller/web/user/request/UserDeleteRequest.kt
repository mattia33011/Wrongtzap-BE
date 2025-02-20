package it.mag.wrongtzap.controller.web.user.request

data class UserDeleteRequest (
    val userId: String,
    val userMail: String,
    val password: String,
)