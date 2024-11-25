package it.mag.wrongtzap.controller.web.exception.user

data class UserNotFoundInAuthentication(override val message: String?) : Exception()