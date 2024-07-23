package it.mag.wrongtzap.service

import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Repository
import org.springframework.stereotype.Service

// Ho tolto l'interfaccia perche' non e' utile in questo caso, non la andiamo a implementare in modi diversi

@Service
class UserService(
    @Autowired
    private val userRepository: UserRepository
) {
    fun generate(user: User) = userRepository.save(user)
    fun getUserById(uid: Long) = userRepository.findById(uid).get()
    fun getByUsername(username: String) = userRepository.findByUsername(username)
    fun deleteByUsername(username: String): String{
        userRepository.deleteByUsername(username)
        return username
    }
}