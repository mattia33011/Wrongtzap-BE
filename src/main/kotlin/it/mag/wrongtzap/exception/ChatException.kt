package it.mag.wrongtzap.exception

open class ChatException: Exception {
    constructor(message: String?): super(message)
    constructor(): super()
}