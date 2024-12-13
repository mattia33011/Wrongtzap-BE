package it.mag.wrongtzap.manager

import it.mag.wrongtzap.controller.web.exception.user.UserNotFoundException
import it.mag.wrongtzap.jwt.Token
import it.mag.wrongtzap.controller.web.response.user.ProfileResponse
import it.mag.wrongtzap.jwt.JwtUtil
import it.mag.wrongtzap.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthManager @Autowired constructor(
    private val jwtUtil: JwtUtil,
    private val userService: UserService,
)
{
    fun fetchProfile(token: Token): ProfileResponse{
        val email = jwtUtil.extractSubject(token.jwt)

        val user = userService.retrieveByEmail(email)
            ?: throw UserNotFoundException()

        return ProfileResponse(
            username = user.username,
            userId = user.userId
        )
    }

    fun checkExpiration(token: Token): Token {
        val expirationDate = Date(System.currentTimeMillis() + 60 * 10 * 1000)
        val tokenExpiration = jwtUtil.extractExpiration(token.jwt)

        val expired = tokenExpiration.before(expirationDate)

        return if(expired)
            refreshToken(token)
        else

            token
    }

    fun refreshToken(token: Token): Token {
        val subject = jwtUtil.extractSubject(token.jwt)
        return Token(jwtUtil.generateToken(subject))
    }

}