package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import jakarta.transaction.Transactional
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers
import kotlin.test.assertNotNull

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
        val mysql: MySQLContainer<Nothing> = MySQLContainer<Nothing>("mysql:latest").apply {
            withInitScript("schema.sql")
        }

        @JvmStatic
        @BeforeAll
        @DynamicPropertySource
        fun dynamicProperties(@Autowired registry: DynamicPropertyRegistry) {
            mysql.start()
            registry.add("spring.datasource.url") { mysql.jdbcUrl }
            registry.add("spring.datasource.username") { mysql.username }
            registry.add("spring.datasource.password") { mysql.password }
            registry.add("spring.datasource.driver-class-name") { mysql.driverClassName }
            registry.add("spring.jpa.hibernate.ddl-auto") { "update" }
            registry.add("spring.jpa.database-platform") { "org.hibernate.dialect.MySQLDialect" }
        }
    }
    /*
        TODO CRUD
            C: CREATE --> DONE
            R: READ --> DONE
            U: UPDATE --> TODO
            D: DELETE --> TODO

            Test Driven Development --> TEST PRIMA DI IMPLEMENTAZIONE
     */

    @Test
    @Transactional
    fun `should save`(){
        service.generate(default1)
        assertNotNull(userRepository)
        val testUser = service.getByUsername(default1.username).firstOrNull()
        assertNotNull(testUser)
    }

}