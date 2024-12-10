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
import org.springframework.web.servlet.config.annotation.CorsRegistry
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer
import kotlin.jvm.Throws
import kotlin.reflect.jvm.internal.impl.load.kotlin.JvmType

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
    fun corsConfigurer(): WebMvcConfigurer {
        return object : WebMvcConfigurer {
            override fun addCorsMappings(registry: CorsRegistry) {
                registry.addMapping("/**")
                    .allowedOrigins("http://localhost:4200")
                    .allowedMethods("*")
                    .allowedHeaders("Authorization", "Content-type")
                    .exposedHeaders("Authorization")
                    .allowCredentials(true).maxAge(3600);
            }
        }
    }

    @Bean
    @Throws(Exception::class)
    fun SecurityFilterChain(http: HttpSecurity): SecurityFilterChain{
        http
            .authorizeHttpRequests{ auth ->
                auth
                    .requestMatchers("/auth/**").permitAll()
                    .requestMatchers("/ws/**").permitAll()
                    .anyRequest().authenticated()
            }
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