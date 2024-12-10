package it.mag.wrongtzap.config

import it.mag.wrongtzap.jwt.JwtHandshakeInterceptor
import it.mag.wrongtzap.jwt.JwtInterceptor
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.Ordered
import org.springframework.core.annotation.Order
import org.springframework.messaging.simp.config.ChannelRegistration
import org.springframework.messaging.simp.config.MessageBrokerRegistry
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker
import org.springframework.web.socket.config.annotation.StompEndpointRegistry
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer


@Configuration
@EnableWebSocketMessageBroker
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
class WebSocketConfig @Autowired constructor(
    private val jwtHandshakeInterceptor: JwtHandshakeInterceptor,

): WebSocketMessageBrokerConfigurer{


    override fun configureMessageBroker(registry: MessageBrokerRegistry) {
        registry.enableSimpleBroker("/topic")
        registry.setApplicationDestinationPrefixes("/api")

    }

    override fun registerStompEndpoints(registry: StompEndpointRegistry) {
        registry.addEndpoint("/ws")
            .setAllowedOrigins("http://localhost:4200")
            .addInterceptors(jwtHandshakeInterceptor)

    }
// DO NOT USE
//    override fun configureClientInboundChannel(registration: ChannelRegistration) {
//        registration.interceptors(jwtInterceptor)
//    }
}