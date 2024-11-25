package it.mag.wrongtzap.controller.web.exception

import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundInAuthentication
import it.mag.wrongtzap.controller.web.exception.user.InvalidEmailFormatException
import it.mag.wrongtzap.controller.web.exception.user.InvalidUserCredentialsException
import it.mag.wrongtzap.controller.web.exception.user.InvalidUsernameFormatException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(it.mag.wrongtzap.controller.web.exception.user.UserNotFoundInAuthentication::class)
    fun userNotFoundAuth(ex: it.mag.wrongtzap.controller.web.exception.user.UserNotFoundInAuthentication):ResponseEntity<Any>{
        return ResponseEntity.status(401).body("User is not authorized")
    }

    @ExceptionHandler(it.mag.wrongtzap.controller.web.exception.user.InvalidUserCredentialsException::class)
    fun InvalidCredentials(ex: it.mag.wrongtzap.controller.web.exception.user.InvalidUserCredentialsException): ResponseEntity<Any>{
        return ResponseEntity.status(401).body("The credentials provided do not match the ones associated to the user")
    }

    @ExceptionHandler(it.mag.wrongtzap.controller.web.exception.user.InvalidEmailFormatException::class)
    fun InvalidEmailForm(ex: it.mag.wrongtzap.controller.web.exception.user.InvalidEmailFormatException): ResponseEntity<Any>{
        return ResponseEntity.status(400).body("Provided email is invalid")
    }

    @ExceptionHandler(it.mag.wrongtzap.controller.web.exception.user.InvalidUsernameFormatException::class)
    fun InvalidUserForm(ex: it.mag.wrongtzap.controller.web.exception.user.InvalidUsernameFormatException): ResponseEntity<Any>{
        return ResponseEntity.status(400).body(
            "Provided username does not conform to format\n" +
                    "Username must not contain special characters\n" +
                    "Username must be between 6 to 20 characters in length\n")
    }
}