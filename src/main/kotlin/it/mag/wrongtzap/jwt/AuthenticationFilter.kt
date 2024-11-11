package it.mag.wrongtzap.jwt

import it.mag.wrongtzap.service.UserDetailService
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

@Component
class AuthenticationFilter @Autowired constructor(
    private val jwtUtil: JwtUtil,
    private val userDetailService: UserDetailService

): OncePerRequestFilter()
{

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val authHeader = request.getHeader("Authorization")
        val jwt: String?
        val userId: String?

        if (authHeader != null && authHeader.startsWith("Bearer ")){
            jwt = authHeader.substring(7)
            userId = jwtUtil.extractSubject(jwt)

            if (userId != null && SecurityContextHolder.getContext().authentication == null){
                val userDetails = userDetailService.loadUserByUserId(userId)

                if(jwtUtil.isValidToken(jwt, userDetails.getId())){
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

