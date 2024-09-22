package it.mag.wrongtzap.controller.web.exception

open class UserException: Exception{
    constructor(message: String?): super(message)
    constructor(): super()
}