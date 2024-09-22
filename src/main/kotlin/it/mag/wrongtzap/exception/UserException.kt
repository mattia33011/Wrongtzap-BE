package it.mag.wrongtzap.exception

open class UserException: Exception{
    constructor(message: String?): super(message)
    constructor(): super()
}