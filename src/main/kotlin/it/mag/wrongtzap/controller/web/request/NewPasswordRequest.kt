package it.mag.wrongtzap.controller.web.request

data class NewPasswordRequest (
    val oldPassword: String,
    val newPassword: String
)