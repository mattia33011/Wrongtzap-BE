package it.mag.wrongtzap.service


import it.mag.wrongtzap.model.User
import org.springframework.stereotype.Service

@Service
interface UserService {
    fun generate(user: User): User
    fun getUserById(uid: Long): User
}
