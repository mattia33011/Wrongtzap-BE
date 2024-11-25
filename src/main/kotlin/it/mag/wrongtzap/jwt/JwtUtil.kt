package it.mag.wrongtzap.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

@Component
class JwtUtil(
    @Value("\${jwt.secret}")
    private val secretKey: String
) {

    fun generateToken(userEmail: String): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, userEmail)
    }

    private fun createToken(claims: Map<String, Any>, userId: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userId)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() + (60 * 60 * 10 * 1000)))
            .signWith(Keys.hmacShaKeyFor(secretKey.encodeToByteArray()))
            .compact()
    }

    fun tokenToSubject(bearer: String): String{
        val token = bearer.substring(7)
        return extractSubject(token)
    }

    fun extractSubject(token: String): String{
        return extractClaim(token, Claims::getSubject)
    }


    fun isTokenExpired(token: String): Boolean{
        return extractExpiration(token).before(Date())
    }

    fun isValidToken(token: String, userEmail: String): Boolean{
        val extractedUsername = extractSubject(token)
        return extractedUsername == userEmail && (!isTokenExpired(token))
    }

    fun extractExpiration(token: String): Date{
        return extractClaim(token, Claims::getExpiration)
    }

    private fun <T> extractClaim(token: String, claimResolver: (Claims) -> T): T
    {
        val claims = extractAllClaims(token)
        return claimResolver.invoke(claims)
    }

    private fun extractAllClaims(token: String): Claims{
        return Jwts.parserBuilder().setSigningKey(Keys.hmacShaKeyFor(secretKey.encodeToByteArray())).build().parse(token).body as Claims
    }

    private fun convertLocalDateTimeNowToDate() = Date(
        LocalDateTime.now()
            .atZone(
                ZoneId.of("CET")
            )
            .plusHours(5)
            .toInstant()
            .toEpochMilli()
    )

}