package it.mag.wrongtzap.jwt

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.messaging.Message
import org.springframework.messaging.MessageChannel
import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaderAccessor
import org.springframework.messaging.support.ChannelInterceptor
import org.springframework.stereotype.Service

@Service
class JwtInterceptor @Autowired constructor(
    private val jwtUtil: JwtUtil,
): ChannelInterceptor {}