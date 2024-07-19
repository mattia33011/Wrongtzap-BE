package it.mag.wrongtzap.controller

import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    lateinit var user_service: UserService

    @GetMapping("/{uid}")
    fun retrieveUser(uid: String) = user_service.getUserById(uid)
    @PostMapping("/stored/users")
    fun storeUser(user: User) = user_service.generate(user)
}