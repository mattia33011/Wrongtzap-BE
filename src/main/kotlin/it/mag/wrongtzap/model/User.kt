package it.mag.wrongtzap.model


import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private val uid: Long?,
    private val username: String,
    private val email: String
) {
    constructor(username: String, email: String) : this(null, username, email)
}