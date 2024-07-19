package it.mag.wrongtzap.repository

import it.mag.wrongtzap.model.User
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository: JpaRepository<User, Long> {
}