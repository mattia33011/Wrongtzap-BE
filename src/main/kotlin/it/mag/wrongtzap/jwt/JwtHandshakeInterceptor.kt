package it.mag.wrongtzap.jwt

import ch.qos.logback.core.util.StringUtil
import it.mag.wrongtzap.service.UserDetailService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.server.ServerHttpRequest
import org.springframework.http.server.ServerHttpResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Service
import org.springframework.util.StringUtils
import org.springframework.web.socket.WebSocketHandler
import org.springframework.web.socket.WebSocketSession
import org.springframework.web.socket.server.HandshakeInterceptor
import java.lang.Exception

@Service
class JwtHandshakeInterceptor @Autowired constructor(
    private val jwtUtil: JwtUtil,
): HandshakeInterceptor
{
    override fun beforeHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        attributes: MutableMap<String, Any>
    ): Boolean {

        val protocols = request.headers["Sec-WebSocket-Protocol"].toString()


        if (protocols.isEmpty()){
            return false
        }

        val jwt = protocols.split(",")

        val token = jwt.first().substring(7)
        val subject = jwtUtil.extractSubject(token)

        return if (jwtUtil.isValidToken(token, subject)) {
            true
        }
        else
            false

    }

    override fun afterHandshake(
        request: ServerHttpRequest,
        response: ServerHttpResponse,
        wsHandler: WebSocketHandler,
        exception: Exception?
    ) {
        //TODO add session time limit
    }

}