package it.mag.wrongtzap.config

import it.mag.wrongtzap.jwt.AuthenticationFilter
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import kotlin.jvm.Throws

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
class SecurityConfig @Autowired constructor(
    private val jwtAuthenticationFilter: AuthenticationFilter,
) {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun SecurityFilterChain(http: HttpSecurity): SecurityFilterChain{
        http
            .authorizeHttpRequests{ auth ->
                auth
                    .requestMatchers("/noauth/**").permitAll()
                    .requestMatchers("/chats/**").permitAll()
                    .anyRequest().authenticated()
            }
            .cors{ it.disable() }
            .csrf { it.disable() }
            .sessionManagement{ session ->
                session.sessionCreationPolicy(
                    SessionCreationPolicy.STATELESS)
            }

        http.addFilterBefore(jwtAuthenticationFilter,UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationManager(authConfig: AuthenticationConfiguration): AuthenticationManager{
        return authConfig.authenticationManager
    }

}