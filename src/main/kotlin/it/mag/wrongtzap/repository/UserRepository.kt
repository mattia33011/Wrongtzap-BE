package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/*
    Ho aggiunto l'annotazione @Repository per rendere questa classe un bean e facilmente iniettabile tramite @Autowired
 */

@Repository
interface UserRepository: JpaRepository<User, Long>{
    fun findByUsername(ciccio: String): List<User>
    fun findByEmail(ciccio: String): User?
    fun findByEmailAndUsername(ciccio: String, pasticcio: String): User?
    fun deleteByUsername(username: String)
}