package it.mag.wrongtzap.controller.web.request.user

data class EditProfileRequest (
    val userId: String,
    val description: String?,
    val username: String?
)