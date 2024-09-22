package it.mag.wrongtzap.request

data class NewPasswordRequest (
    val oldPassword: String,
    val newPassword: String
)