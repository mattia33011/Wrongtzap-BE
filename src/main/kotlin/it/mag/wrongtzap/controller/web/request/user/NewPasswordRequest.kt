package it.mag.wrongtzap.controller.web.request.user

data class NewPasswordRequest (
    val userId: String,
    val oldPassword: String,
    val newPassword: String
)