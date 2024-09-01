package it.mag.wrongtzap.service

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService @Autowired constructor(
    private val userService: UserService
) : UserDetailsService {

    override fun loadUserByUsername(userId: String): UserDetails {
        val user = userService.retrieveById(userId)
            ?: throw NullPointerException("User Not Found")

        return CustomUserDetails(user)

    }

}