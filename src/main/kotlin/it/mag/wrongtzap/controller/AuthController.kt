package it.mag.wrongtzap.controller

import it.mag.wrongtzap.jwt.JwtUtil
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
class AuthController @Autowired constructor(
    val userDetailsService: UserDetailsService,
    val jwtUtil: JwtUtil,
    val authenticationManager: AuthenticationManager
) {
    @PostMapping("")

}