package it.mag.wrongtzap.controller.web.request.user

data class NewPasswordRequest (
    val oldPassword: String,
    val newPassword: String
)