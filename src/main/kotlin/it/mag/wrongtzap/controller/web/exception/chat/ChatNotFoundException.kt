package it.mag.wrongtzap.controller.web.exception.chat

import it.mag.wrongtzap.controller.web.exception.ChatException

class ChatNotFoundException: it.mag.wrongtzap.controller.web.exception.ChatException {
    constructor(message: String?): super(message)
    constructor(): super()
}