package it.mag.wrongtzap.exception

import it.mag.wrongtzap.exception.user.UserNotFoundInAuthentication
import it.mag.wrongtzap.exception.user.InvalidEmailFormatException
import it.mag.wrongtzap.exception.user.InvalidUserCredentialsException
import it.mag.wrongtzap.exception.user.InvalidUsernameFormatException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(UserNotFoundInAuthentication::class)
    fun userNotFoundAuth(ex: UserNotFoundInAuthentication):ResponseEntity<Any>{
        return ResponseEntity.status(401).body("User is not authorized")
    }

    @ExceptionHandler(InvalidUserCredentialsException::class)
    fun InvalidCredentials(ex: InvalidUserCredentialsException): ResponseEntity<Any>{
        return ResponseEntity.status(401).body("The credentials provided do not match the ones associated to the user")
    }

    @ExceptionHandler(InvalidEmailFormatException::class)
    fun InvalidEmailForm(ex: InvalidEmailFormatException): ResponseEntity<Any>{
        return ResponseEntity.status(400).body("Provided email is invalid")
    }

    @ExceptionHandler(InvalidUsernameFormatException::class)
    fun InvalidUserForm(ex: InvalidUsernameFormatException): ResponseEntity<Any>{
        return ResponseEntity.status(400).body(
            "Provided username does not conform to format\n" +
                    "Username must not contain special characters\n" +
                    "Username must be between 6 to 20 characters in length\n")
    }
}