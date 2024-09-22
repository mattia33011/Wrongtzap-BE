package it.mag.wrongtzap.exception.user

data class UserNotFoundInAuthentication(override val message: String?) : Exception()