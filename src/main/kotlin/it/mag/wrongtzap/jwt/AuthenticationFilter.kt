package it.mag.wrongtzap.jwt

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter

class AuthenticationFilter @Autowired constructor(
    private val jwtUtil: JwtUtil,
    private val userDetailsService: UserDetailsService

): OncePerRequestFilter()
{
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("auth")
        val jwt: String?
        val userId: String?

        if (authHeader != null && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7)
            userId = jwtUtil.extractUserId(jwt)

            if (userId != null && SecurityContextHolder.getContext().authentication == null){
                val userDetails = userDetailsService.loadUserByUsername(userId)

                if(jwtUtil.isValidToken(jwt, userDetails.username)){
                    val authToken = UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.authorities
                    )
                    authToken.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authToken
                }
            }
        }
        filterChain.doFilter(request, response)
    }

}

