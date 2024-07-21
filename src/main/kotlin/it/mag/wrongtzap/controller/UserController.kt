package it.mag.wrongtzap.controller

import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

/*
    @author Mattia Iaria

    INFO:
        Come vedi ho spostato il path nell'annotazione di @RestController cosi' per accedere a tutti questi endpoint si utilizzera'
        il prefix /users
    ESEMPIO:
        per accedere alla post dovrai fare:
        curl -X POST http://localhost:8080/users

        invece per la get tramite uid:
        curl http://localhost:8080/users/{uid}

     Note:
        non dimenticare di mettere l'annotazione @RequestBody per richiedere un body di un endpoint
 */
@RestController
@RequestMapping("/users")
class UserController(
    @Autowired
    val userService: UserService
) {
    @GetMapping("/{uid}")
    fun retrieveUser(@PathVariable uid:Long) = userService.getUserById(uid)
    @PostMapping
    fun storeUser(@RequestBody user: User) = userService.generate(user)
}