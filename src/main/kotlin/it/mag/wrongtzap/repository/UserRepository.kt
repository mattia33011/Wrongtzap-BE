package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository: JpaRepository<User, String>{
    fun findByUserName(UserId: String): List<User>
    fun deleteByUserNameAndUserMail(userName: String, userMail: String): User

}