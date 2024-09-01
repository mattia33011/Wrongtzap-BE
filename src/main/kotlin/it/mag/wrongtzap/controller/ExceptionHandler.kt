package it.mag.wrongtzap.controller

import it.mag.wrongtzap.controller.exception.UserNotFoundInAuthentication
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseBody

@ControllerAdvice
class ExceptionHandler {
    @ExceptionHandler(UserNotFoundInAuthentication::class)
    fun userNotFoundAuth(ex: UserNotFoundInAuthentication):ResponseEntity<Any>{
        return ResponseEntity.status(401).body("Utente Non Autorizzato")
    }
}