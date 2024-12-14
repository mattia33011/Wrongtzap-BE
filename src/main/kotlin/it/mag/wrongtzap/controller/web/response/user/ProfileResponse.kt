package it.mag.wrongtzap.controller.web.response.user

import jakarta.persistence.Embeddable


@Embeddable
data class ProfileResponse (
    val userId: String,
    val username: String
)