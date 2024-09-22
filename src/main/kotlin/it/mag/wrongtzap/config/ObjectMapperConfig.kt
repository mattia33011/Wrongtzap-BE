package it.mag.wrongtzap.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class ObjectMapperConfig {

    @Bean
    fun objectMapperBean(): ObjectMapper {

        return jacksonObjectMapper().apply {

            registerModule(JavaTimeModule())
            registerModule(KotlinModule())
            disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        }

    }

}