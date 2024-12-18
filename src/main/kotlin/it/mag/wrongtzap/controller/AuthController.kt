package it.mag.wrongtzap.controller

import com.fasterxml.jackson.annotation.JsonView
import it.mag.wrongtzap.config.ViewsConfig
import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.controller.web.request.LoginRequest
import it.mag.wrongtzap.controller.web.request.RegisterRequest
import it.mag.wrongtzap.jwt.Token
import it.mag.wrongtzap.manager.SessionManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
    val userManager: UserManager,
    val sessionManager: SessionManager
) {

    @PostMapping("/register")
    @JsonView(ViewsConfig.Public::class)
    fun register(@RequestBody registerRequest: RegisterRequest) = userManager.createUser(registerRequest)

    @PostMapping("/login")
    fun login(@RequestBody userCredentials: LoginRequest) = userManager.login(userCredentials)

    @PostMapping("/profile")
    fun profile(@RequestBody token: Token) = sessionManager.fetchProfile(token)

    @PostMapping("/refresh")
    fun refresh(@RequestBody token: Token) = sessionManager.checkExpiration(token)
}