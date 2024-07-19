package it.mag.wrongtzap.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import javax.print.attribute.standard.RequestingUserName

@Entity
class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)

    lateinit var username: String
    lateinit var email: String
    lateinit var uid: String
}