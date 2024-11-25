package it.mag.wrongtzap.controller.web.exception

open class MessageException: Exception {
    constructor(message: String?): super(message)
    constructor(): super()
}