package it.mag.wrongtzap.service

import it.mag.wrongtzap.exception.user.UserNotFoundException
import it.mag.wrongtzap.jwt.UserDetail
import it.mag.wrongtzap.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailService @Autowired constructor(
    private val userRepository: UserRepository
) : UserDetailsService {

    override fun loadUserByUsername(userMail: String): UserDetails {
        val user = userRepository.findByEmail(userMail)
            ?: throw UserNotFoundException("User does not exist")

        return UserDetail(user.username, user.password,user.email, true, listOf())
    }

    fun loadUserByEmail(userMail: String): UserDetail{
        val user = userRepository.findByEmail(userMail)
            ?: throw UserNotFoundException("User does not exist")

        return UserDetail(
            username = user.username,
            email = user.email,
            password = user.password,
            enabled = true,
            roles = listOf()
        )

    }

}