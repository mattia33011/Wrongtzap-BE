package it.mag.wrongtzap.controller.web.exception.user

import it.mag.wrongtzap.controller.web.exception.UserException

class InvalidUsernameFormatException: it.mag.wrongtzap.controller.web.exception.UserException {
    constructor(message: String?): super(message)
    constructor(): super()
}