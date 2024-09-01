package it.mag.wrongtzap.unit

import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import it.mag.wrongtzap.model.User
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MappingTest {
    @Test
    fun generateKey(){
        val key = Keys.secretKeyFor(SignatureAlgorithm.HS256)
    }

}