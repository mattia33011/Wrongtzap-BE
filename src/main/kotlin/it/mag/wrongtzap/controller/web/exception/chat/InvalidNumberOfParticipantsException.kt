package it.mag.wrongtzap.controller.web.exception.chat

import it.mag.wrongtzap.controller.web.exception.ChatException

class InvalidNumberOfParticipantsException: it.mag.wrongtzap.controller.web.exception.ChatException {
    constructor(message:String?): super(message)
    constructor(): super()
}