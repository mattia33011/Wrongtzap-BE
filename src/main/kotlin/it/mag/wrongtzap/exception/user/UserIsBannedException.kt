package it.mag.wrongtzap.exception.user

import it.mag.wrongtzap.exception.UserException

class UserIsBannedException: UserException {
    constructor(message: String?): super(message)
    constructor(): super()
}