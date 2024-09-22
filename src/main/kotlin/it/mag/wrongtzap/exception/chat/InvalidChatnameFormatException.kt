package it.mag.wrongtzap.exception.chat

import it.mag.wrongtzap.exception.ChatException

class InvalidChatnameFormatException: ChatException {
    constructor(message: String?): super(message)
    constructor(): super()
}