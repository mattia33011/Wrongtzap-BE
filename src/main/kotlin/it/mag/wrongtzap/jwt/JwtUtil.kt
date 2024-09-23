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

    fun generateToken(userId: String): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, userId)
    }

    private fun createToken(claims: Map<String, Any>, userId: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userId)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(convertLocalDateTimeNowToDate())
            .signWith(Keys.hmacShaKeyFor(secretKey.encodeToByteArray()))
            .compact()
    }

    fun fullTokenToId(bearer: String): String{
        val token = bearer.substring(7)
        return extractUserId(token)
    }

    fun extractUserId(token: String): String{
        return extractClaim(token, Claims::getSubject)
    }

    fun isTokenExpired(token: String): Boolean{
        return extractExpiration(token).before(Date())
    }

    fun isValidToken(token: String, userId: String): Boolean{
        val extractedUsername = extractUserId(token)
        return extractedUsername == userId && (!isTokenExpired(token))
    }

    private fun extractExpiration(token: String): Date{
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