package it.mag.wrongtzap.controller.web.exception

open class ChatException: Exception {
    constructor(message: String?): super(message)
    constructor(): super()
}