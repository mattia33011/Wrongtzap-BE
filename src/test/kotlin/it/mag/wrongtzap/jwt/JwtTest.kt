package it.mag.wrongtzap.jwt

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals

@SpringBootTest(classes = [JwtUtil::class])
class JwtTest(
    @Autowired private val jwtUtil: JwtUtil
) {
    @Test
    fun test(){
        val generated = jwtUtil.generateToken("Ciao")
        assertEquals("Ciao", jwtUtil.extractUserId(generated))

    }
}