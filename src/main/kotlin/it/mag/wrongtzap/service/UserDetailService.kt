package it.mag.wrongtzap.service

import it.mag.wrongtzap.controller.exception.UserNotFoundInAuthentication
import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailService @Autowired constructor(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(userId: String): org.springframework.security.core.userdetails.UserDetails {
        val user = retrieveById(userId)
        return it.mag.wrongtzap.jwt.UserDetail(user.userName, user.userPassword, true, listOf())
    }

    fun retrieveById(userId: String): User = userRepository.findById(userId).orElseThrow { UserNotFoundInAuthentication("User Not Found") }

}