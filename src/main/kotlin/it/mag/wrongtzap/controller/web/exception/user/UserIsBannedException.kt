package it.mag.wrongtzap.controller.web.exception.user

import it.mag.wrongtzap.controller.web.exception.UserException

class UserIsBannedException: it.mag.wrongtzap.controller.web.exception.UserException {
    constructor(message: String?): super(message)
    constructor(): super()
}