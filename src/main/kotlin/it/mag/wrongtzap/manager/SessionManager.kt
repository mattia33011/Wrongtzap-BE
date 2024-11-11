package it.mag.wrongtzap.manager

import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.controller.web.response.TokenResponse
import it.mag.wrongtzap.controller.web.response.UserDisplayResponse
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class SessionManager @Autowired constructor(
    private val jwtUtil: JwtUtil,
    private val userService: UserService,
)
{
    fun getDisplayData(token: TokenResponse): UserDisplayResponse{
        val email = jwtUtil.extractSubject(token.token)

        val user = userService.retrieveByEmail(email)
            ?: throw UserNotFoundException()

        return UserDisplayResponse(
            username = user.username,
            userId = user.userId
        )
    }

    fun checkExpiration(tokenResponse: TokenResponse): TokenResponse{
        val expirationDate = Date(System.currentTimeMillis() + 60 * 10 * 1000)
        val tokenExpiration = jwtUtil.extractExpiration(tokenResponse.token)

        val expired = tokenExpiration.before(expirationDate)

        return if(expired)
            refreshToken(tokenResponse)
        else
            tokenResponse
    }

    fun refreshToken(token: TokenResponse): TokenResponse{
        val subject = jwtUtil.extractSubject(token.token)
        return TokenResponse(jwtUtil.generateToken(subject))
    }

}