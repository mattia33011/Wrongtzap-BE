package it.mag.wrongtzap.controller

import it.mag.wrongtzap.controller.web.response.TokenResponse
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.manager.SessionManager
import it.mag.wrongtzap.manager.UserManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/session")
class SessionController @Autowired constructor(
    private val sessionManager: SessionManager
){
    @PostMapping("/user-display")
    fun getDisplayData(@RequestBody tokenResponse: TokenResponse) = sessionManager.getDisplayData(tokenResponse)

    @PostMapping("/check-expiration")
    fun check(@RequestBody tokenResponse: TokenResponse) = sessionManager.checkExpiration(tokenResponse)
}