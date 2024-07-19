package it.mag.wrongtzap.controller

import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController("/users")
class UserController(
    @Autowired
    val userService: UserService
) {
    @GetMapping("/{uid}")
    fun retrieveUser(@PathVariable uid:Long) = userService.getUserById(uid)
    @PostMapping
    fun storeUser(user: User) = userService.generate(user)
    @GetMapping("/users")
    fun getAll() = userService.getAll()
}