package it.mag.wrongtzap.controller.web.user.request

data class NewPasswordRequest (
    val userId: String,
    val oldPassword: String,
    val newPassword: String
)