package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.request.UserRequest
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/noauth")
class AuthController @Autowired constructor(
    val userManager: UserManager
) {
    @PostMapping("/login")
    //TODO REQUEST BODY
    fun login(@RequestParam userMail: String, password: String): String{
        return userManager.login(userMail, password)
    }

    @PostMapping("/register")
    @JsonView(ViewsConfig.Public::class)
    fun register(@RequestBody userRequest: UserRequest) = userManager.createUser(userRequest)

}