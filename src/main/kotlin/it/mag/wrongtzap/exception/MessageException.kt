package it.mag.wrongtzap.exception

open class MessageException: Exception {
    constructor(message: String?): super(message)
    constructor(): super()
}