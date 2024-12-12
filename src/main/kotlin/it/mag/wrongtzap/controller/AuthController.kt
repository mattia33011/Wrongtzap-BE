package it.mag.wrongtzap.controller

import it.mag.wrongtzap.manager.UserManager
import it.mag.wrongtzap.controller.web.request.user.LoginRequest
import it.mag.wrongtzap.controller.web.request.user.RegisterRequest
import it.mag.wrongtzap.jwt.Token
import it.mag.wrongtzap.manager.AuthManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
    val userManager: UserManager,
    val sessionManager: AuthManager
) {

    @PostMapping("/register")
    fun register(@RequestBody registerRequest: RegisterRequest) = userManager.createUser(registerRequest)

    @PostMapping("/login")
    fun login(@RequestBody userCredentials: LoginRequest) = userManager.login(userCredentials)

    @PostMapping("/profile")
    fun profile(@RequestBody token: Token) = sessionManager.fetchProfile(token)

    @PostMapping("/refresh")
    fun refresh(@RequestBody token: Token) = sessionManager.checkExpiration(token)
}