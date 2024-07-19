package it.mag.wrongtzap.model


import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity

class User(uid: Long, private val username: String, private val email: String) {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val uid: Long = uid
}