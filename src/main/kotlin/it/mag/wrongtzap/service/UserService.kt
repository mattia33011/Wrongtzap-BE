package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import it.mag.wrongtzap.service.UserService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
    @Autowired
    val userRepository: UserRepository
) {


    fun generate(user: User) = userRepository.save(user)

    fun getUserById(uid: Long) = userRepository.findById(uid).get()

    fun getAll(): List<User> {
        return userRepository.findAll()
    }
}