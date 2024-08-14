package it.mag.wrongtzap.repository

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

// MockK
// Mock Bean


@SpringBootTest
class UserRepositoryTest(
    @Autowired
    private val userRepository: UserRepository
) {
    @Test
    fun repositoryTest(){
        val user = userRepository.findByUserName("Mattia").firstOrNull()
        assertNotNull(user)
        assertEquals("Mattia", user.userName)
        assertEquals("Mattia.iaria30@gmail.com", user.userMail)
    }
}