package it.mag.wrongtzap.exception.user

import it.mag.wrongtzap.exception.UserException

class InvalidUserCredentialsException: UserException{
    constructor(message: String?): super(message)
    constructor(): super()
}