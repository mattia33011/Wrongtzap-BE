package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.controller.web.request.LoginRequest
import it.mag.wrongtzap.controller.web.request.UserRequest
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/noauth")
class AuthController @Autowired constructor(
    val userManager: UserManager
) {
    @PostMapping("/login")
    fun login(@RequestBody userCredentials: LoginRequest) = userManager.login(userCredentials)

    @PostMapping("/register")
    @JsonView(ViewsConfig.Public::class)
    fun register(@RequestBody userRequest: UserRequest) = userManager.createUser(userRequest)
}