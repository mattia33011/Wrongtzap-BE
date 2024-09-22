package it.mag.wrongtzap.exception.user

import it.mag.wrongtzap.exception.UserException

class UserNotFoundException: UserException{
    constructor(message: String?): super(message)
    constructor(): super()
}