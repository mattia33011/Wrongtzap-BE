package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, String>{

    fun findByUsername(UserId: String): List<User>
    fun findByPasswordAndEmail(userPassword: String, userMail: String): User
    fun findByEmail(userMail: String): User?
    fun deleteByUsernameAndEmail(userName: String, userMail: String): User

}


