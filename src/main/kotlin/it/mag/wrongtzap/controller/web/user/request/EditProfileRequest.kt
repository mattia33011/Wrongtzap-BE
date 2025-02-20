package it.mag.wrongtzap.controller.web.user.request

data class EditProfileRequest (
    val userId: String,
    val description: String?,
    val username: String?
)