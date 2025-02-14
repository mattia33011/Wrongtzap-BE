package it.mag.wrongtzap.controller.web.request.user

data class EditProfileRequest (
    val userId: Long,
    val description: String?,
    val username: String?
)