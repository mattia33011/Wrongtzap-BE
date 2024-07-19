package it.mag.wrongtzap.controller

import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class UserController {

    @Autowired
    lateinit var user_service: UserService

    @GetMapping("/{uid}")
    fun retrieveUser(@PathVariable uid:Long) = user_service.getUserById(uid)
    @PostMapping("/stored/users")
    fun storeUser(user: User) = user_service.generate(user)

    @GetMapping("/users")
    fun s(): Any{
        return user_service.getAll()
    }

    //MEtodo per test, da cancellare
    @PostMapping("/users")
    fun post(): Any{
        return user_service.generate(User(1,"Mattia","Mattia.iaria@email.it"))
    }
}