package it.mag.wrongtzap.exception.message

import it.mag.wrongtzap.exception.MessageException

class MessageNotFoundException: MessageException {
    constructor(message: String?): super(message)
    constructor(): super()
}