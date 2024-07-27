package it.mag.wrongtzap.service

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import jakarta.transaction.Transactional
import net.bytebuddy.utility.dispatcher.JavaDispatcher.Container
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.internal.verification.NoInteractions
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.testcontainers.context.ImportTestcontainers
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.data.jpa.repository.Query
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertIsNot
import kotlin.test.assertNotNull
import kotlin.test.assertNull

@SpringBootTest
@Testcontainers
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class UserServiceTest @Autowired constructor(
    val service: UserService,
    val userRepository: UserRepository,
) {

    companion object Database{

        val default1 =User("mattia", "somemail@gmail.com")
        val default2= User("Gabriele", "1234@libero.it")
        val default3= User("Alex" ,"trythis@arubapec.it")

        @Container
        lateinit var mysql: MySQLContainer<Nothing>

        @BeforeAll
        @JvmStatic
        fun init() {
            mysql = MySQLContainer<Nothing>("mysql:latest").apply {
                withInitScript("schema.sql")
            }
            mysql.start()

        }

        @DynamicPropertySource
        @JvmStatic
        fun dynamicProperties(registry: DynamicPropertyRegistry) {
            registry.add("spring.datasource.url") { mysql.jdbcUrl }
            registry.add("spring.datasource.username") { mysql.username }
            registry.add("spring.datasource.password") { mysql.password }
            registry.add("spring.datasource.driver-class-name") { mysql.driverClassName }
            registry.add("spring.jpa.hibernate.ddl-auto") { "update" }
            registry.add("spring.jpa.database-platform") { "org.hibernate.dialect.MySQLDialect" }
        }
    }

    @Test
    @Transactional
    fun `should save`(){


        service.generate(default1)
        assertNotNull(userRepository)

        val testUser = service.getByUsername(default1.username).firstOrNull()
        assertNotNull(testUser)

    }

}