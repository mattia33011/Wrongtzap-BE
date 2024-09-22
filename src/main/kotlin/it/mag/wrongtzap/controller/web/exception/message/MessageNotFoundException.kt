package it.mag.wrongtzap.controller.web.exception.message

import it.mag.wrongtzap.controller.web.exception.MessageException

class MessageNotFoundException: it.mag.wrongtzap.controller.web.exception.MessageException {
    constructor(message: String?): super(message)
    constructor(): super()
}