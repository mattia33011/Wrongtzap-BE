package it.mag.wrongtzap.controller.web.request

data class TokenExpirationRequest(
    val token: String,
    val expiration: String
)