package it.mag.wrongtzap.controller.web.request.user

data class UserDeleteRequest (
    val userId: String,
    val userMail: String,
    val password: String,
)