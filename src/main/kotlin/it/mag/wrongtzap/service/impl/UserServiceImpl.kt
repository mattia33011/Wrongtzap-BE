package it.mag.wrongtzap.service.impl

import it.mag.wrongtzap.model.User
import it.mag.wrongtzap.repository.UserRepository
import it.mag.wrongtzap.service.UserService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserServiceImpl: UserService {

    @Autowired
    lateinit var user_repository: UserRepository

    @Override
    override fun generate(user: User) = user_repository.save(user)

    @Override
    override fun getUserById(uid: String) = user_repository.findById(uid).get()
}