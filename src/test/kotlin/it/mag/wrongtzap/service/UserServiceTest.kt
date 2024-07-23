package it.mag.wrongtzap.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
class UserServiceTest(
    @Autowired
    private val service: UserService,

) {
    val user = User("Gabriele", "vincenzo.cascio@email.it")

    @MockK
    private val userRepository: UserRepository = mockk()

    @Test
    @Transactional
    fun `should save`(){

        every { userRepository.findByUsername(any()) }.returns(
            listOf(user)
        )

        service.generate(user)

        val testUser = service.getByUsername(user.username).firstOrNull()
        assertNotNull(testUser)

        service.deleteByUsername(testUser.username)
        assertNull(service.getByUsername(testUser.username).firstOrNull())
    }

}