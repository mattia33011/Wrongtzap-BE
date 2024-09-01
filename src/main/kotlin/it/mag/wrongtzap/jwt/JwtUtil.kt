package it.mag.wrongtzap.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JwtUtil {

    private val secretKey: Key = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun generateToken(userId: String): String {
        val claims = HashMap<String, Any>()
        return createToken(claims, userId)
    }

    private fun createToken(claims: Map<String, Any>, userId: String): String {
        return Jwts.builder()
            .setClaims(claims)
            .setSubject(userId)
            .setIssuedAt(Date(System.currentTimeMillis()))
            .setExpiration(Date(System.currentTimeMillis() * 1000 * 60 * 60 * 5 ))
            .signWith(secretKey)
            .compact()
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
        return Jwts.parserBuilder().setSigningKey(secretKey).build().parseClaimsJwt(token).body
    }


}