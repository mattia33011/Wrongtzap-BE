package it.mag.wrongtzap.unit

import it.mag.wrongtzap.model.User
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class MappingTest {
    @Test
    fun tes(){
        val u = User("", "")
        assertEquals(u.mappingToString(), "ciao")
    }

}