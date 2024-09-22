package it.mag.wrongtzap.exception.chat

import it.mag.wrongtzap.exception.ChatException

class UserNotFoundInChat: ChatException {
    constructor(message: String?): super(message)
    constructor(): super()
}