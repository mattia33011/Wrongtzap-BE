package it.mag.wrongtzap.controller.exception

data class UserNotFoundInAuthentication(override val message: String?) : Exception()